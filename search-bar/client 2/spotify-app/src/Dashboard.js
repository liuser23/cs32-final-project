import React, {useState, useEffect} from 'react'
import {View, Text} from 'react-native'
import useAuth from './useAuth'
import {Container, Form} from 'react-bootstrap'
import SpotifyWebApi from "spotify-web-api-node"
import TrackSearchResult from "./TrackSearchResult";
import Player from "./Player";
import axios from 'axios'
import Recommendations from "./Recommendations";
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography'
import Box from '@mui/material/Box';
import {useTheme, ThemeProvider, createTheme} from '@mui/material/styles';
import Brightness4Icon from '@mui/icons-material/Brightness4';
import Brightness7Icon from '@mui/icons-material/Brightness7';
import { amber, deepOrange, grey } from '@mui/material/colors';
// import PaletteMode from '@mui/material';
import PaletteMode from '@mui/material'
import { palette } from '@mui/system';
// import { PaletteMode } from '@material-ui/core';
import TodoList from './TodoList';
import FormRecs from './Form'
import {v4 as uuidv4} from 'uuid'
import TextField from '@material-ui/core/TextField';
import {makeStyles} from "@material-ui/core/styles";
import {Button, IconButton} from '@material-ui/core'
import DeleteIcon from '@material-ui/icons/Delete';
import Add from '@material-ui/icons/Add'
import Switch, {SwitchProps} from '@mui/material/Switch'
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import PlayCircleOutlineIcon from '@material-ui/icons/PlayCircleOutline'
import SpotifyLogo from './spotify-logo.png'
import HomeIcon from '@material-ui/icons/Home'
import PlaylistAddIcon from '@mui/icons-material/PlaylistAdd';
import PlaylistAddCheckRoundedIcon from '@mui/icons-material/PlaylistAddCheckRounded';
import LooksOneIcon from '@mui/icons-material/LooksOne'
import LooksTwoIcon from '@mui/icons-material/LooksTwo'
import ColorPicker from 'material-ui-color-picker'


import Stack from '@mui/material/Stack';
// import Typography from '@mui/material/Typography';
import CoolSwitch from './Switch'

// import { makeStyles } from '@material-ui/core/styles';

//import DeleteIcon from '@material-ui/core/IconButton'
// import Delete from '@mui/icons-material/'
import AddIcon from '@material-ui/core/IconButton'
import {LooksOne, LooksTwo, Looks3} from "@material-ui/icons";
// const useStyles = makeStyles((theme) => ({
//     margin: {
//         margin: theme.spacing(1),
//     },
//     extendedIcon: {
//         marginRight: theme.spacing(1),
//     },
// }));

const spotifyApi = new SpotifyWebApi({
    clientId: "786d7d05062645e09e6cc0f9df174325",
})

// const darkTheme = createTheme({
//     palette: {
//         mode: 'dark',
//     },
// });

const ColorModeContext = React.createContext({
    toggleColorMode: () => {
    }
});

const getDesignTokens = (mode: PaletteMode) => ({
    palette: {
        mode,
        ...(mode === 'light'
            ? {
                // palette values for light mode
                primary: amber,
                divider: amber[200],
                text: {
                    primary: grey[900],
                    secondary: grey[800],
                },
            }
            : {
                // palette values for dark mode
                primary: deepOrange,
                divider: deepOrange[700],
                background: {
                    default: deepOrange[900],
                    paper: deepOrange[900],
                },
                text: {
                    primary: '#fff',
                    secondary: grey[500],
                },
            }),
    },
});

const useStyles = makeStyles((theme) => ({
    root: {
        '& .MuiTextField-root': {
            margin: theme.spacing(1)
        }
    },
    margin: {
        margin: theme.spacing(1),
    },
    extendedIcon: {
        marginRight: theme.spacing(1),
    },
    customHoverFocus: {
        margin: theme.spacing(1),
        "&:hover, &.Mui-focusVisible": {backgroundColor: "#1DB954"}
    },
    customHoverFocusNoMargin: {
        "&:hover, &.Mui-focusVisible": {backgroundColor: "#1DB954"}
    },
    viewStyleForLine: {
        borderBottomColor: "black",
        borderBottomWidth: StyleSheet.hairlineWidth,
        alignSelf: 'stretch',
        width: "100%"
    },
    icons: {
        color: "black",
        fontSize: '40px'
    },
    homeIcon: {
        color: "#1DB954",
        fontSize: '100px',
        margin: '10px'
    }
}))

export default function Dashboard({code}) {
    let darkModeOn = false;
    const theme = useTheme();
    const colorMode = React.useContext(ColorModeContext);

    function getRandomInt(max) {
        return Math.floor(Math.random() * max);
    }

    function toggleDarkMode() {
        if (darkModeOn) {
            darkModeOn = false;
        } else {
            darkModeOn = true;
        }

    }

    const classes = useStyles()
    const accessToken = useAuth(code)
    const [search, setSearch] = useState("")
    const [searchUser, setSearchUser] = useState("")
    const [searchRecs, setSearchRecs] = useState("")
    const [searchResults, setSearchResults] = useState([])
    const [searchUserResults, setSearchUserResults] = useState([])
    const [searchResultsRecommendation, setSearchResultsRecommendation] = useState([])
    const [playingTrack, setPlayingTrack] = useState()
    const [lyrics, setLyrics] = useState("")
    const [listRecommendation, setListRecommendations] = useState([])
    let searchArray = [];
    let searchRecsArray = [];
    let searchUserArray = [];

    const starterRecs = [
        {
            trackName: "First Recommendation Title",
            artistName: "First Recommendation Artist",
            isUsed: false,
            albumArt: "",
            track: null,
            id: 0
        },
        {
            trackName: "Second Recommendation Title",
            artistName: "Second Recommendation Artist",
            isUsed: false,
            albumArt: "",
            track: null,
            id: 1
        },
        {
            trackName: "Third Recommendation Title",
            artistName: "Third Recommendation Artist",
            isUsed: false,
            albumArt: "",
            track: null,
            id: 2
        },

    ];

    const [list, setList] = useState(["First Recommendation", "Second Recommendation", "Third Recommendation"])
    //const [list1, setList1] = useState([{trackName: ""}, {artistName: ""}, {isUsed: false}])
    const [list1, setList1] = useState(starterRecs)


    //const [listRecs2, setListRecs2] = useState([])
    // ([defaultString, defaultString, defaultString])
    let topSong1 = [{trackName: "Top Song"}, {artistName: "CS32"}, {isUsed: false}]
    let topSong2 = [{trackName: "Top Song 2"}, {artistName: "CS32"}, {isUsed: false}]
    let topSong3 = [{trackName: "Top Song 3"}, {artistName: "CS32"}, {isUsed: false}]

    const [topSongs, setTopSongs] = useState([topSong1, topSong2, topSong3])

    let placeHolderTop3List = [{topSong1, topSong2, topSong3}]
    //let listRecs = list.map((item) => <ListItem key={getRandomInt(1000000)}> <ListItemText {item} />{item}</ListItem>);
    // let listRecs1 = list1.map((item) => <li key={getRandomInt(1000000)}>{item.trackName}</li>)

    let topSongsList = topSongs.map((song) =>
        <ListItem key={getRandomInt(1000000)} alignItems="flex-start"
                  style={{margin: "8px", padding: "20px", border: '1px solid rgba(0, 0, 0, 0.1)'}}
                  className={classes.customHoverFocus}
                  secondaryAction={
                      <React.Fragment>
                          <IconButton onClick={() => chooseTrack(song.track)} edge="end" aria-label="play">
                              <PlayCircleOutlineIcon style={{marginLeft: "10px"}}/>
                          </IconButton>
                          <IconButton onClick={() => addToPlaylist()} edge="end" aria-label="Play">
                              <PlaylistAddIcon/>
                          </IconButton>
                      </React.Fragment>
                  }>
            <ListItemAvatar>
                <Avatar class="material-icons">
                    <LooksOne className={classes.icons}/>
                </Avatar>
            </ListItemAvatar>
            <ListItemText primary={song.trackName ? song.trackName : "Song Title"}
                          secondary={song.artistName ? song.artistName : "Song Artist"} style={{color: "black"}}/>
        </ListItem>
    )

    let listRecs2 = list1.map((song) =>
        <ListItem key={getRandomInt(1000000)} alignItems="flex-start"
                  style={{margin: "8px", padding: "20px", border: '1px solid rgba(0, 0, 0, 0.1)'}}
                  className={classes.customHoverFocus}
                  secondaryAction={
                      <React.Fragment>
                          <IconButton onClick={() => chooseTrack(song.track)} edge="end" aria-label="play">
                              <PlayCircleOutlineIcon style={{margin: "10px"}}/>
                          </IconButton>
                          <IconButton onClick={() => addToPlaylist()} edge="end" aria-label="Play">
                              <PlaylistAddIcon/>
                          </IconButton>
                          <IconButton onClick={() => handleDelete(song)} edge="end" aria-label="delete">
                              <DeleteIcon style={{margin: "20px"}}/>
                          </IconButton>
                      </React.Fragment>
                  }>
            <ListItemAvatar>
                <Avatar sx={{height: '60px', width: '60px', marginRight: "10px"}} alt="Album cover"
                        src={song.albumArt ? song.albumArt : SpotifyLogo}/>
            </ListItemAvatar>
            <ListItemText primary={song.trackName ? song.trackName : "Song Title"}
                          secondary={song.artistName ? song.artistName : "Song Artist"} style={{color: "black"}}/>
        </ListItem>
    )

    function reRenderList() {
        listRecs2 = list1.map((song) =>
            <ListItem key={getRandomInt(1000000)} alignItems="flex-start"
                      style={{margin: "8px", padding: "20px", border: '1px solid rgba(0, 0, 0, 0.1)'}}
                      className={classes.customHoverFocus}
                      secondaryAction={
                          <React.Fragment>
                              <IconButton onClick={() => chooseTrack(song.track)} edge="end" aria-label="play">
                                  <PlayCircleOutlineIcon style={{margin: "10px"}}/>
                              </IconButton>
                              <IconButton onClick={() => addToPlaylist()} edge="end" aria-label="Play">
                                  <PlaylistAddIcon/>
                              </IconButton>
                              <IconButton onClick={() => handleDelete(song)} edge="end" aria-label="delete">
                                  <DeleteIcon style={{margin: "20px"}}/>
                              </IconButton>
                          </React.Fragment>
                      }>
                <ListItemAvatar>
                    <Avatar sx={{height: '60px', width: '60px', marginRight: "10px"}} alt="Album cover"
                            src={song.albumArt ? song.albumArt : SpotifyLogo}/>
                </ListItemAvatar>
                <ListItemText primary={song.trackName ? song.trackName : "Song Title"}
                              secondary={song.artistName ? song.artistName : "Song Artist"} style={{color: "black"}}/>
            </ListItem>
        )
        setListRecommendations(listRecs2)
    }


    // setListRecommendations(
    //     list1.map((song) =>
    //     <ListItem key={getRandomInt(1000000)} alignItems="flex-start"
    //               style={{margin: "8px", padding: "20px", border: '1px solid rgba(0, 0, 0, 0.1)'}}
    //               className={classes.customHoverFocus}
    //               secondaryAction={
    //                   <React.Fragment>
    //                       <IconButton onClick={() => chooseTrack(song.track)} edge="end" aria-label="play">
    //                           <PlayCircleOutlineIcon style={{margin: "10px"}}/>
    //                       </IconButton>
    //                       <IconButton onClick={() => addToPlaylist()} edge="end" aria-label="Play">
    //                           <PlaylistAddIcon/>
    //                       </IconButton>
    //                       <IconButton onClick={() => handleDelete(song.trackName)} edge="end" aria-label="delete">
    //                           <DeleteIcon style={{margin: "20px"}}/>
    //                       </IconButton>
    //                   </React.Fragment>
    //               }>
    //         <ListItemAvatar>
    //             <Avatar sx={{ height: '60px', width: '60px', marginRight: "10px" }} alt="Album cover" src={song.albumArt? song.albumArt : SpotifyLogo}/>
    //         </ListItemAvatar>
    //         <ListItemText primary={song.trackName? song.trackName : "Song Title"} secondary={song.artistName? song.artistName : "Song Artist"} style={{color: "black"}}/>
    //     </ListItem>
    // ))


    function startPlaying(trackName) {
        console.log("PLAYING + " + trackName.trackName)
        console.log("PLAYING TRACK B4 + " + playingTrack.title)

        setPlayingTrack(trackName.uri)
        chooseTrack(trackName)
        console.log("PLAYING TRACK AFTER + " + playingTrack.title)

        setSearch("")
        setLyrics("")
    }

    function chooseTrack(track) {
        setListRecommendations(listRecs2)
        console.log("choosing + " + track.title)
        setPlayingTrack(track)
        setSearch("")
        setLyrics("")
    }

    function addToRecList(track) {
        setSearchRecs("")
        if (track === null) {
            console.log("song must have title")
        } else {


            const trackTitle = track.title;
            const artist = track.artist;

            if (list1.length < 3) {
                console.log("no items in list yet")
                let song = {}
                list1.push(song)
            }
            let tempList = list1
            // let tempList = list1.map(item => ({...item}))
            for (let i = 0; i < list1.length; i++) {
                console.log("List1 Before Adding: Song " + i + ": " + list1[i].trackName)
            }

            if ((tempList[0]?.trackName === trackTitle && tempList[0]?.isUsed) || (tempList[1]?.trackName === trackTitle && tempList[1]?.isUsed) || (tempList[2]?.trackName === trackTitle && tempList[2]?.isUsed)) {
                console.log("can't repeat recommendations")
                console.log("list1[0].trackName: " + tempList[0]?.trackName)
                console.log("list1[1].trackName: " + tempList[1]?.trackName)
                console.log("list1[2].trackName: " + tempList[2]?.trackName)
            } else if (tempList[0]?.isUsed != true) {
                tempList[0].trackName = trackTitle;
                tempList[0].artistName = artist
                tempList[0].isUsed = true
                tempList[0].albumArt = track.albumUrl
                tempList[0].track = track
                console.log("added 1st rec")
                console.log("list1[0].isUsed : " + list1[0].isUsed)
                console.log("ID" + list1[0].id);
                setList1(tempList)
                // list1[0].albumURL = albumArt
            } else if (tempList[1]?.isUsed != true) {
                tempList[1].trackName = trackTitle;
                tempList[1].artistName = artist
                tempList[1].isUsed = true
                tempList[1].albumArt = track.albumUrl
                tempList[1].track = track
                console.log("added 2nd rec")
                setList1(tempList)

                // list1[1].albumURL = albumArt
            } else if (tempList[2]?.isUsed != true) {
                tempList[2].trackName = trackTitle;
                tempList[2].artistName = artist
                tempList[2].isUsed = true
                tempList[2].albumArt = track.albumUrl
                tempList[2].track = track
                console.log("added 3rd rec")
                setList1(tempList)

                // list1[2].albumURL =albumArt
            } else {
                console.log("Too many song recs")
            }
            for (let i = 0; i < tempList.length; i++) {
                console.log("tempList After Adding: Song " + i + ": " + tempList[i].trackName)
            }

            for (let i = 0; i < list1.length; i++) {
                console.log("List1 After Adding: Song " + i + ": " + list1[i].trackName)
            }
            // setListRecommendations(listRecs2)
            reRenderList()
        }
    }

    const handleDelete = (song) => {
        console.log("song to delete trackname" + song.trackName)
        // const newListRecs = listRecs2.filter((record) => record.trackName !== song)
        // const songToDelete = listRecs2.filter((record) => record.trackName === song)
        // const songToDelete = listRecommendation.filter((record) => record.trackName === song)
        console.log("listRecommendation b4" + listRecommendation)
        let newListRecs = listRecommendation.filter((record) => record.trackName !== song.trackName)
        console.log("newListRecs after" + newListRecs)

        for (let i = 0; i < listRecommendation.length; i++) {
            console.log("Song " + i + ": " + listRecommendation[i].trackName)
        }

        // console.log("songToDelete" + songToDelete.trackName)
        // console.log("listRecs2 b4 " + listRecs2[0].MuiListItemText)
        // listRecs2 = newListRecs

        // console.log("listRecs2 after" + listRecs2)
        // list1 = list1.filter((song) => song.id !== id) )
        //let tempList = list1

        // let tempList = list1.map(item => ({...item}))

        // if (tempList[0].trackName === song.trackName) {
        //     console.log("songToDelete is in spot 0")
        //     tempList[0].trackName = "";
        //     tempList[0].artistName = "";
        //     tempList[0].isUsed = false;
        //     tempList[0].albumArt = SpotifyLogo;
        // } else if (tempList[1].trackName === song.trackName) {
        //     console.log("songToDelete is in spot 1")
        //     tempList[1].trackName = "";
        //     tempList[1].artistName = "";
        //     tempList[1].isUsed = false;
        //     tempList[1].albumArt = SpotifyLogo;
        // } else if (tempList[2].trackName === song.trackName) {
        //     console.log("songToDelete is in spot 2")
        //     tempList[2].trackName = "";
        //     tempList[2].artistName = "";
        //     tempList[2].isUsed = false;
        //     tempList[2].albumArt = SpotifyLogo;
        // } else {
        //     console.log("deletion error")
        // }
        let listToFilter = list1
        let tempList = listToFilter.filter((record) => record.trackName !== song.trackName)
        for (let i = 0; i < tempList.length; i++) {
            console.log("tempList After Deletion: Song " + i + ": " + tempList[i].trackName)
        }
        setList1(prevList => prevList.filter((record) => record.trackName !== song.trackName))
        // setList1(prevList => prevList.filter((record) => record.trackName !== song.trackName))

        //setList1(tempList)
        for (let i = 0; i < list1.length; i++) {
            console.log("List1 After Deletion: Song " + i + ": " + list1[i].trackName)
        }
        //setListRecommendations(newListRecs)
        reRenderList()

    }

    // const ListTag = () => list.map(item => (<li>{item}</li>));

    function addToPlaylist() {
        // playlist ID 7xrIGuwvonS17zWjYzpUeR?si=665954b6a7874cf8
        // song uri https://open.spotify.com/track/0xcl9XT60Siji6CSG4y6nb?si=9be52965d755468d
        // 0xcl9XT60Siji6CSG4y6nb?si=9be52965d755468d
        console.log("trying to add to playlist")
        // spotifyApi.createPlaylist('My playlist 2', {'description': 'My description', 'collaborative' : false, 'public': true}).then(function(data){
        //     console.log("added new playlist!");
        //     const playlistId = data.id;
        //     console.log(playlistId)
        // }, function (err) {
        //     console.log("something went wrong!", err);
        // })

        spotifyApi.addTracksToPlaylist('7xrIGuwvonS17zWjYzpUeR?si=665954b6a7874cf8', '["spotify:track:0xcl9XT60Siji6CSG4y6nb?si=9be52965d755468d"]').then(function (data) {
            console.log('Added tracks to playlist!');
        }, function (err) {
            console.log('Something went wrong!', err);
        });
    }


    useEffect(() => {
        if (!playingTrack) return
        axios
            .get("http://localhost:3001/lyrics", {
                params: {
                    track: playingTrack.title,
                    artist: playingTrack.artist,
                },
            })
            .then(res => {
                setLyrics(res.data.lyrics)
            })
    }, [playingTrack])

    useEffect(() => {
        if (!accessToken) return
        spotifyApi.setAccessToken(accessToken)
    }, [accessToken])

    useEffect(() => {
        if (!search) return setSearchResults([])
        if (!accessToken) return
        let cancel = false;
        spotifyApi.searchTracks(search).then(res => {
            if (cancel) return
            searchArray = res.body.tracks.items;
            console.log("search array: " + searchArray)
            setSearchResults(res.body.tracks.items.map(track => {
                const smallestAlbumImage = track.album.images[0];
                // const smallestAlbumImage = track.album.images.reduce((smallest, image) => {
                //     if (image.height < smallest.height) return image
                //     return smallest
                // }, track.album.images[0])

                return {
                    artist: track.artists[0].name,
                    title: track.name,
                    uri: track.uri,
                    albumUrl: smallestAlbumImage.url
                }
            }))
        })

        return () => cancel = true
    }, [search, accessToken])

    //
    // useEffect(() => {
    //     if (!searchUser) return setSearchUserResults([])
    //     if (!accessToken) return
    //     let cancel = false;
    //     spotifyApi.getUser(searchUser).then(res => {
    //         if (cancel) return
    //         searchUserArray = res.body
    //     })
    // })


    useEffect(() => {
        if (!searchRecs) return setSearchResultsRecommendation([])
        if (!accessToken) return
        let cancel = false;
        spotifyApi.searchTracks(searchRecs).then(res => {
            if (cancel) return
            searchRecsArray = res.body.tracks.items;
            console.log("search array: " + searchRecsArray)
            setSearchResultsRecommendation(res.body.tracks.items.map(track => {
                // const smallestAlbumImage = track.album.images[0]
                const smallestAlbumImage = track.album.images.reduce((smallest, image) => {
                    if (image.height < smallest.height) return image
                    return smallest
                }, track.album.images[0])
                return {
                    artist: track.artists[0].name,
                    title: track.name,
                    uri: track.uri,
                    albumUrl: smallestAlbumImage.url
                    // currPlayingSongAlbumURI: smallestAlbumImage.url
                }

            }))
        })

        return () => cancel = true
    }, [searchRecs, accessToken])

    function ToggleColorMode() {
        const [mode, setMode] = React.useState < 'light' | 'dark' > ('light');
        const colorMode = React.useMemo(
            () => ({
                toggleColorMode: () => {
                    setMode((prevMode) => (prevMode === 'light' ? 'dark' : 'light'));
                },
            }),
            [],
        );

        const theme = React.useMemo(
            () =>
                createTheme({
                    palette: {
                        mode,
                    },
                }),
            [mode],
        );

    }

    return (
        // darkModeOn ? 'black' : 'white'
        <ColorModeContext.Provider value={colorMode}>
            <ThemeProvider theme={theme}>
                <div style={{background: darkModeOn ? 'black' : 'white'}}>
                    <Container className="d-flex flex-column py-2" style={{height: "100vh"}}>
                        <div className="top-bar">
                            <IconButton style={{color: "#1DB954"}} edge="end"
                                        aria-label="Home">
                                <HomeIcon/>
                            </IconButton>
                            {theme.palette.mode} mode
                            <IconButton sx={{ml: 1}} onClick={colorMode.toggleColorMode} color="inherit">
                                {theme.palette.mode === 'dark' ? <Brightness7Icon/> : <Brightness4Icon/>}
                            </IconButton>
                            {/*<FormGroup>*/}
                            {/*    <FormControlLabel control={*/}
                            {/*            <Switch*/}
                            {/*                defaultChecked*/}
                            {/*                onChange={toggleDarkMode}*/}
                            {/*            />}*/}
                            {/*         label="Dark Mode" styles={{color: darkModeOn ? 'black' : 'white'}}/>*/}
                            {/*</FormGroup>*/}
                        </div>
                        {/*<FormGroup>*/}
                        {/*    /!*<FormControlLabel*!/*/}
                        {/*    /!*    control={<CoolSwitch sx={{ m: 1 }} defaultChecked onChange={toggleDarkMode}/>}*!/*/}
                        {/*    /!*//*/}
                {/*    <FormControlLabel control={*/}
                        {/*        <Switch*/}
                        {/*            defaultChecked*/}
                        {/*            onChange={toggleDarkMode}*/}
                        {/*        />*/}
                        {/*    } label="Dark Mode" styles={{color: darkModeOn ? 'black' : 'white'}}/>*/}
                        {/*</FormGroup>*/}
                        <Form.Control
                            type="search"
                            placeholder="Search Songs/Albums"
                            value={search}
                            onChange={e => setSearch(e.target.value)}
                        />

                        <div className="flex-grow-1 my-2" style={{overflowY: "auto"}}>
                            {searchResults.map(track => (
                                <TrackSearchResult
                                    track={track}
                                    key={track.uri}
                                    chooseTrack={chooseTrack}
                                    isRec={false}
                                />
                            ))}
                            {searchResults.length === 0 && (
                                <React.Fragment>
                                    <div
                                        style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}
                                    >
                                        <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>

                                        <div>
                                            <h3 style={{textAlign: 'center', color: "#1DB954"}}>Top Recommendations</h3>
                                        </div>

                                        <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>
                                    </div>
                                    <div className="playing-and-top-recs">
                                        <div className="currently-playing">
                                            {/*style={{height: "250px", width: "250px"}}   */}
                                            <img src={playingTrack?.albumUrl}
                                                 style={{
                                                     height: "300px",
                                                     width: "300px",
                                                     border: '10px solid rgb(0, 0, 0)'
                                                 }}/>
                                            <List sx={{width: '300px'}}>
                                                <ListItem className={classes.customHoverFocusNoMargin}
                                                          style={{border: '1px solid rgba(0, 0, 0, 0.1)'}}>
                                                    {/*<ListItemText primary={playingTrack?.title : "Song Title"} secondary={playingTrack.artist? playingTrack.artist : "Song Artist"} style={{color: "black"}}/>*/}
                                                    <ListItemText primary={playingTrack?.title}
                                                                  secondary={playingTrack?.artist}/>
                                                    <IconButton onClick={() => addToRecList(playingTrack)} edge="end"
                                                                aria-label="add">
                                                        <Add/>
                                                    </IconButton>
                                                    <IconButton onClick={() => addToPlaylist()} edge="end"
                                                                aria-label="Play">
                                                        <PlaylistAddIcon/>
                                                    </IconButton>
                                                </ListItem>
                                            </List>
                                        </div>
                                        <div className="topSongsList">
                                            <List sx={{width: '450px', padding: "10px"}}>
                                                {topSongsList}
                                            </List>
                                        </div>
                                    </div>
                                    <div>
                                        <div
                                            style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}
                                        >
                                            <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>

                                            <div>
                                                <h3 style={{textAlign: 'center', color: "#1DB954"}}>My
                                                    Recommendations</h3>
                                            </div>

                                            <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>
                                        </div>
                                        <div className="adding-song">
                                            {/*className={classes.root}*/}
                                            <Form.Control
                                                type="search"
                                                placeholder="Search Songs to Recommend"
                                                value={searchRecs}
                                                onChange={e => setSearchRecs(e.target.value)}
                                            />
                                        </div>
                                        <div className="flex-grow-1 my-2" style={{overflowY: "auto"}}>
                                            {searchResultsRecommendation.map(track => (
                                                <TrackSearchResult
                                                    track={track}
                                                    key={track.uri}
                                                    chooseTrack={addToRecList}
                                                    isRec={true}
                                                />
                                            ))}
                                        </div>
                                        <div className="recsList">
                                            <List sx={{width: '100%', padding: "10px"}}>
                                                {/*{listRecommendation}*/}
                                                {list1.map((song) =>
                                                    <ListItem key={getRandomInt(1000000)} alignItems="flex-start"
                                                              style={{margin: "8px", padding: "20px", border: '1px solid rgba(0, 0, 0, 0.1)'}}
                                                              className={classes.customHoverFocus}
                                                              secondaryAction={
                                                                  <React.Fragment>
                                                                      <IconButton onClick={() => chooseTrack(song.track)} edge="end" aria-label="play">
                                                                          <PlayCircleOutlineIcon style={{margin: "10px"}}/>
                                                                      </IconButton>
                                                                      <IconButton onClick={() => addToPlaylist()} edge="end" aria-label="Play">
                                                                          <PlaylistAddIcon/>
                                                                      </IconButton>
                                                                      <IconButton onClick={() => handleDelete(song)} edge="end" aria-label="delete">
                                                                          <DeleteIcon style={{margin: "20px"}}/>
                                                                      </IconButton>
                                                                  </React.Fragment>
                                                              }>
                                                        <ListItemAvatar>
                                                            <Avatar sx={{height: '60px', width: '60px', marginRight: "10px"}} alt="Album cover"
                                                                    src={song.albumArt ? song.albumArt : SpotifyLogo}/>
                                                        </ListItemAvatar>
                                                        <ListItemText primary={song.trackName ? song.trackName : "Song Title"}
                                                                      secondary={song.artistName ? song.artistName : "Song Artist"} style={{color: "black"}}/>
                                                    </ListItem>
                                                )}
                                            </List>
                                        </div>
                                    </div>
                                    <div className="text-center" style={{whiteSpace: "pre"}}>
                                        <div
                                            style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}
                                        >
                                            <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>

                                            <div>
                                                <h3 style={{textAlign: 'center', color: "#1DB954"}}>Lyrics</h3>
                                            </div>

                                            <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>
                                        </div>
                                        <div className="color-lyrics">
                                            {/*<ColorPicker*/}
                                            {/*    name='color'*/}
                                            {/*    defaultValue='#000'*/}
                                            {/*    // value={this.state.color} - for controlled component*/}
                                            {/*    onChange={color => lyrics.set({color: color})}*/}
                                            {/*/>*/}
                                            {/*<div className={classes.customHoverFocusNoMargin}>*/}
                                            <div>

                                                <Typography variant="h1">{playingTrack?.title}</Typography>
                                                <Typography varient="overline">{playingTrack?.artist}</Typography>
                                            </div>

                                            <br/>
                                            {lyrics}
                                        </div>
                                    </div>
                                </React.Fragment>
                            )}

                        </div>
                        <div>
                            <Player accessToken={accessToken} trackUri={playingTrack?.uri}/>
                        </div>
                    </Container>
                </div>
            </ThemeProvider>
        </ColorModeContext.Provider>
    )
}


//// Get an artist's top tracks
// spotifyApi.getArtistTopTracks('0oSGxfWSnnOXhD2fKuz2Gy', 'GB')
//   .then(function(data) {
//     console.log(data.body);
//     }, function(err) {
//     console.log('Something went wrong!', err);
//   });

{/*    <div className="top-recs">*/
}
{/*        <List sx={{width: '350px'}}>*/
}
{/*            <ListItem className={classes.customHoverFocus}*/
}
{/*                      style={{border: '1px solid rgba(0, 0, 0, 0.1)', margin: "20px"}}*/
}
{/*                      secondaryAction={*/
}
{/*                          <React.Fragment>*/
}
{/*                              <IconButton onClick={() => addToPlaylist()} edge="end"*/
}
{/*                                          aria-label="Play">*/
}
{/*                                  <PlaylistAddIcon/>*/
}
{/*                              </IconButton>*/
}
{/*                              <IconButton onClick={() => addToRecList()} edge="end"*/
}
{/*                                          aria-label="add">*/
}
{/*                                  <Add/>*/
}
{/*                              </IconButton>*/
}
{/*                          </React.Fragment>}>*/
}
{/*                <ListItemAvatar>*/
}
{/*                    <Avatar class="material-icons">*/
}
{/*                        <LooksOne className={classes.icons}/>*/
}
{/*                    </Avatar>*/
}
{/*                </ListItemAvatar>*/
}
{/*                <ListItemText primary="Song Number 1" secondary="Arist Number 1"/>*/
}
{/*            </ListItem>*/
}
{/*            <ListItem className={classes.customHoverFocus}*/
}
{/*                      style={{border: '1px solid rgba(0, 0, 0, 0.1)', margin: "20px"}}*/
}
{/*                      secondaryAction={*/
}
{/*                          <React.Fragment>*/
}
{/*                              <IconButton onClick={() => addToPlaylist()} edge="end"*/
}
{/*                                          aria-label="Play">*/
}
{/*                                  <PlaylistAddIcon/>*/
}
{/*                              </IconButton>*/
}
{/*                              <IconButton onClick={() => addToRecList()} edge="end" aria-label="add">*/
}
{/*                                  <Add/>*/
}
{/*                              </IconButton>*/
}
{/*                          </React.Fragment>}>*/
}
{/*                <ListItemAvatar>*/
}
{/*                    <Avatar class="material-icons">*/
}
{/*                        <LooksTwo className={classes.icons}/>*/
}
{/*                    </Avatar>*/
}
{/*                </ListItemAvatar>*/
}
{/*                <ListItemText primary="Song Number 2" secondary="Artist Number 2"/>*/
}
{/*            </ListItem>*/
}
{/*            <ListItem className={classes.customHoverFocus}*/
}
{/*                      style={{border: '1px solid rgba(0, 0, 0, 0.1)', margin: "20px"}}*/
}
{/*                      secondaryAction={*/
}
{/*                          <React.Fragment>*/
}
{/*                              <IconButton onClick={() => addToPlaylist()} edge="end"*/
}
{/*                                          aria-label="Play">*/
}
{/*                                  <PlaylistAddIcon/>*/
}
{/*                              </IconButton>*/
}
{/*                              <IconButton onClick={() => addToRecList()} edge="end" aria-label="add">*/
}
{/*                                  <Add/>*/
}
{/*                              </IconButton>*/
}
{/*                          </React.Fragment>}>*/
}
{/*                <ListItemAvatar>*/
}
{/*                    <Avatar class='material-icons'>*/
}
{/*                        <Looks3 className={classes.icons}/>*/
}
{/*                    </Avatar>*/
}
{/*                </ListItemAvatar>*/
}
{/*                <ListItemText primary="Song Number 3" secondary="Artist Number 3"/>*/
}
{/*            </ListItem>*/
}
{/*        </List>*/
}
{/*    </div>*/
}

// if ((list1[0].trackName === trackTitle && list1[0].isUsed) || (list1[1].trackName === trackTitle && list1[1].isUsed) || (list1[2].trackName === trackTitle && list1[2].isUsed)) {
//     console.log("can't repeat recommendations")
//     console.log("list1[0].trackName: " + list1[0].trackName)
//     console.log("list1[1].trackName: " + list1[1].trackName)
//     console.log("list1[2].trackName: " + list1[2].trackName)
// } else if (list1[0].isUsed != true) {
//     list1[0].trackName = trackTitle;
//     list1[0].artistName = artist
//     list1[0].isUsed = true
//     list1[0].albumArt = track.albumUrl
//     list1[0].track = track
//     console.log("added 1st rec")
//     console.log("list1[0].isUsed : " + list1[0].isUsed)
//     console.log("ID" + list1[0].id);
//     // list1[0].albumURL = albumArt
// } else if (list1[1].isUsed != true) {
//     list1[1].trackName = trackTitle;
//     list1[1].artistName = artist
//     list1[1].isUsed = true
//     list1[1].albumArt = track.albumUrl
//     list1[1].track = track
//     console.log("added 2nd rec")
//
//     // list1[1].albumURL = albumArt
// } else if (list1[2].isUsed != true) {
//     list1[2].trackName = trackTitle;
//     list1[2].artistName = artist
//     list1[2].isUsed = true
//     list1[2].albumArt = track.albumUrl
//     list1[2].track = track
//     console.log("added 3rd rec")
//
//     // list1[2].albumURL =albumArt
// } else {
//     console.log("Too many song recs")
// }

// if (tempList[0].trackName === song) {
//     console.log("songToDelete is in spot 0")
//     tempList[0].trackName = "";
//     tempList[0].artistName = "";
//     tempList[0].isUsed = false;
//     tempList[0].albumArt = SpotifyLogo;
// } else if (tempList[1].trackName === song) {
//     console.log("songToDelete is in spot 1")
//     tempList[1].trackName = "";
//     tempList[1].artistName = "";
//     tempList[1].isUsed = false;
//     tempList[1].albumArt = SpotifyLogo;
// } else if (tempList[2].trackName === song) {
//     console.log("songToDelete is in spot 2")
//     tempList[2].trackName = "";
//     tempList[2].artistName = "";
//     tempList[2].isUsed = false;
//     tempList[2].albumArt = SpotifyLogo;
// } else {
//     console.log("deletion error")
// }