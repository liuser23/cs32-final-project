import React, {useState, useEffect} from 'react'
import {View, Text} from 'react-native'
import {Container, Form} from 'react-bootstrap'
import SpotifyWebApi from "spotify-web-api-node"
import TrackSearchResult from "./TrackSearchResult";
import Player from "./Player";
import axios from 'axios'
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
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
import lyricsFinder from 'lyrics-finder'


import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
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
        fontSize: '40px'
    }
}))

export default function Dashboard({code, accessToken, nowPlaying, setNowPlaying}) {
    let darkModeOn = false;

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
    const [search, setSearch] = useState("")
    const [searchUser, setSearchUser] = useState("")
    const [searchRecs, setSearchRecs] = useState("")
    const [searchResults, setSearchResults] = useState([])
    const [searchUserResults, setSearchUserResults] = useState([])
    const [searchResultsRecommendation, setSearchResultsRecommendation] = useState([])
    const [lyrics, setLyrics] = useState("")
    const [recommendations, setRecommendation] = useState("")

    let searchArray = [];
    let searchRecsArray = [];
    let searchUserArray = [];
    //let list = ["hello", "hello2", "hello3"]
    // const [list, setList] = useState(String[3]);
    const [list, setList] = useState(["First Recommendation", "Second Recommendation", "Third Recommendation"])
    const [list1, setList1] = useState([{trackName: ""}, {artistName: ""}, {isUsed: false}])
    //const [listRecs2, setListRecs2] = useState([])
    // ([defaultString, defaultString, defaultString])
    let topSong1 = [{trackName: "Top Song"}, {artistName: "CS32"}, {isUsed: false}]
    let topSong2 = [{trackName: "Top Song 2"}, {artistName: "CS32"}, {isUsed: false}]
    let topSong3 = [{trackName: "Top Song 3"}, {artistName: "CS32"}, {isUsed: false}]

    const[topSongs, setTopSongs] = useState([topSong1, topSong2, topSong3])

    let placeHolderTop3List = [{topSong1, topSong2, topSong3}]
    //let listRecs = list.map((item) => <ListItem key={getRandomInt(1000000)}> <ListItemText {item} />{item}</ListItem>);
    // let listRecs1 = list1.map((item) => <li key={getRandomInt(1000000)}>{item.trackName}</li>)

    let topSongsList = topSongs.map((song) =>
        <ListItem key={getRandomInt(1000000)} alignItems="flex-start"
                  style={{margin: "8px", padding: "20px", border: '1px solid rgba(0, 0, 0, 0.1)'}}
                  className={classes.customHoverFocus}
                  secondaryAction={
                      <React.Fragment>
                          <IconButton onClick={() => startPlaying(song)} edge="end" aria-label="play">
                              <PlayCircleOutlineIcon style={{marginLeft: "10px"}}/>
                          </IconButton>
                          <IconButton onClick={() => addToPlaylist()} edge="end" aria-label="Play">
                              <PlaylistAddIcon/>
                          </IconButton>
                          <IconButton onClick={() => handleDelete(song.trackName)} edge="end" aria-label="delete">
                              <DeleteIcon style={{marginRight: "10px"}}/>
                          </IconButton>
                      </React.Fragment>
                  }>
            <ListItemAvatar>
                <Avatar class="material-icons">
                    <LooksOne className={classes.icons}/>
                </Avatar>
            </ListItemAvatar>
            <ListItemText primary={song.trackName} secondary={song.artistName} style={{color: "black"}}/>
        </ListItem>
    )

    let listRecs2 = list1.map((song) =>
        <ListItem key={getRandomInt(1000000)} alignItems="flex-start"
                  style={{margin: "8px", padding: "20px", border: '1px solid rgba(0, 0, 0, 0.1)'}}
                  className={classes.customHoverFocus}
                  secondaryAction={
                      <React.Fragment>
                          <IconButton onClick={() => startPlaying(song)} edge="end" aria-label="play">
                              <PlayCircleOutlineIcon style={{margin: "10px"}}/>
                          </IconButton>
                          <IconButton onClick={() => addToPlaylist()} edge="end" aria-label="Play">
                              <PlaylistAddIcon/>
                          </IconButton>
                          <IconButton onClick={() => handleDelete(song.trackName)} edge="end" aria-label="delete">
                              <DeleteIcon style={{margin: "20px"}}/>
                          </IconButton>
                      </React.Fragment>
                  }>
            <ListItemAvatar>
                <Avatar sx={{ height: '60px', width: '60px', marginRight: "10px" }} alt="Album cover" src={song.albumArt}/>
            </ListItemAvatar>
            <ListItemText primary={song.trackName} secondary={song.artistName} style={{color: "black"}}/>
        </ListItem>
    )



    function startPlaying(trackName) {
        console.log("PLAYING + " + trackName.trackName)
        console.log("PLAYING TRACK B4 + " + nowPlaying.name)

        // setNowPlaying(trackName.uri)
        chooseTrack(trackName)
        console.log("PLAYING TRACK AFTER + " + nowPlaying.name)

        setSearch("")
        setLyrics("")
    }

    function chooseTrack(track) {
        console.log("choosing + ", track)
        setNowPlaying(track)

        // setPlayingTrack(track)
        // setCurrPlayingTrack(track)
        // setCurrPlayingSong(track.title)
        // currPlayingSongTitle = track.title;
        // setCurrPlayingSongArtist(track.artist)
        // //currPlayingSongArtist = track.artist;
        // setCurrPlayingSongAlbumArt(track.albumUrl)
        // console.log("currPlayingSong + " + currPlayingSong)
        //
        // console.log("currPlayingSongTitle + " + currPlayingSongTitle)
        // console.log("currPlayingSongArtist + " + currPlayingSongArtist)
        setSearch("")
        setLyrics("")
    }

    function addToRecList(track) {
        setSearchRecs("")
        if (track === null) {
            console.log("song must have title")
        } else {


            const trackTitle = track.name;
            const artist = track.artists[0].name;


            if ((list1[0].trackName === trackTitle && list1[0].isUsed) || (list1[1].trackName === trackTitle && list1[1].isUsed) || (list1[2].trackName === trackTitle && list1[2].isUsed)) {
                console.log("can't repeat recommendations")
                console.log("list1[0].trackName: " + list1[0].trackName)
                console.log("list1[1].trackName: " + list1[1].trackName)
                console.log("list1[2].trackName: " + list1[2].trackName)
            } else if (list1[0].isUsed != true) {
                list1[0].trackName = trackTitle;
                list1[0].artistName = artist
                list1[0].isUsed = true
                list1[0].albumArt = track.albumUrl
                console.log("added 1st rec")
                console.log("list1[0].isUsed : " + list1[0].isUsed)
                console.log("ID" + list1[0].id);
                // list1[0].albumURL = albumArt
            } else if (list1[1].isUsed != true) {
                list1[1].trackName = trackTitle;
                list1[1].artistName = artist
                list1[1].isUsed = true
                list1[1].albumArt = track.albumUrl
                console.log("added 2nd rec")

                // list1[1].albumURL = albumArt
            } else if (list1[2].isUsed != true) {
                list1[2].trackName = trackTitle;
                list1[2].artistName = artist
                list1[2].isUsed = true
                list1[2].albumArt = track.albumUrl
                console.log("added 3rd rec")

                // list1[2].albumURL =albumArt
            } else {
                console.log("Too many song recs")
            }
        }
    }

    const handleDelete = (song) => {
        console.log("song.trackname" + song)
        const newListRecs = listRecs2.filter((record) => record.trackName !== song)
        const songToDelete = listRecs2.filter((record) => record.trackName === song)
        console.log("songToDelete" + songToDelete)
        console.log("listRecs2 b4 " + listRecs2[0].MuiListItemText)
        listRecs2 = newListRecs
        console.log("listRecs2 after" + listRecs2)
        // list1 = list1.filter((song) => song.id !== id) )

        if (list1[0].trackName == song) {
            console.log("songToDelete is in spot 0")
            list1[0].trackName = "";
            list1[0].artistName = "";
            list1[0].isUsed = false;
        } else if (list1[1].trackName == song) {
            console.log("songToDelete is in spot 1")
            list1[1].trackName = "";
            list1[1].artistName = "";
            list1[1].isUsed = false;
        } else if (list1[2].trackName == song) {
            console.log("songToDelete is in spot 2")
            list1[2].trackName = "";
            list1[2].artistName = "";
            list1[2].isUsed = false;
        } else {
            console.log("deletion error")
        }
        addToRecList(null)
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


    useEffect(async () => {
        if (!nowPlaying) return

        const lyrics = (await lyricsFinder(playingTrack.artists[0].name, playingTrack.name)) || "No Lyrics Found"
        setLyrics(lyrics)
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

                return track
            }))
        })

        return () => cancel = true
    }, [searchRecs, accessToken])

    return (
        <div style={{background: darkModeOn ? 'black' : 'white'}}>
            <Container className="d-flex flex-column py-2" style={{height: "100vh"}}>
                <div className="top-bar">
                    <IconButton className={classes.homeIcon} edge="end"
                                aria-label="Home">
                        <HomeIcon/>
                    </IconButton>
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
                </div>
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
                                    <img src={nowPlaying.images[0]}
                                         style={{height: "300px", width: "300px", border: '10px solid rgb(0, 0, 0)'}}/>
                                    <List sx={{width: '300px'}}>
                                        <ListItem className={classes.customHoverFocusNoMargin}
                                                  style={{border: '1px solid rgba(0, 0, 0, 0.1)'}}>
                                            <ListItemText primary={nowPlaying.name} secondary={nowPlaying.artists[0].name}/>
                                            <IconButton onClick={() => addToRecList(nowPlaying)} edge="end"
                                                        aria-label="add">
                                                <Add/>
                                            </IconButton>
                                            <IconButton onClick={() => addToPlaylist()} edge="end" aria-label="Play">
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
                            {/*    <div className="top-recs">*/}
                            {/*        <List sx={{width: '350px'}}>*/}
                            {/*            <ListItem className={classes.customHoverFocus}*/}
                            {/*                      style={{border: '1px solid rgba(0, 0, 0, 0.1)', margin: "20px"}}*/}
                            {/*                      secondaryAction={*/}
                            {/*                          <React.Fragment>*/}
                            {/*                              <IconButton onClick={() => addToPlaylist()} edge="end"*/}
                            {/*                                          aria-label="Play">*/}
                            {/*                                  <PlaylistAddIcon/>*/}
                            {/*                              </IconButton>*/}
                            {/*                              <IconButton onClick={() => addToRecList()} edge="end"*/}
                            {/*                                          aria-label="add">*/}
                            {/*                                  <Add/>*/}
                            {/*                              </IconButton>*/}
                            {/*                          </React.Fragment>}>*/}
                            {/*                <ListItemAvatar>*/}
                            {/*                    <Avatar class="material-icons">*/}
                            {/*                        <LooksOne className={classes.icons}/>*/}
                            {/*                    </Avatar>*/}
                            {/*                </ListItemAvatar>*/}
                            {/*                <ListItemText primary="Song Number 1" secondary="Arist Number 1"/>*/}
                            {/*            </ListItem>*/}
                            {/*            <ListItem className={classes.customHoverFocus}*/}
                            {/*                      style={{border: '1px solid rgba(0, 0, 0, 0.1)', margin: "20px"}}*/}
                            {/*                      secondaryAction={*/}
                            {/*                          <React.Fragment>*/}
                            {/*                              <IconButton onClick={() => addToPlaylist()} edge="end"*/}
                            {/*                                          aria-label="Play">*/}
                            {/*                                  <PlaylistAddIcon/>*/}
                            {/*                              </IconButton>*/}
                            {/*                              <IconButton onClick={() => addToRecList()} edge="end" aria-label="add">*/}
                            {/*                                  <Add/>*/}
                            {/*                              </IconButton>*/}
                            {/*                          </React.Fragment>}>*/}
                            {/*                <ListItemAvatar>*/}
                            {/*                    <Avatar class="material-icons">*/}
                            {/*                        <LooksTwo className={classes.icons}/>*/}
                            {/*                    </Avatar>*/}
                            {/*                </ListItemAvatar>*/}
                            {/*                <ListItemText primary="Song Number 2" secondary="Artist Number 2"/>*/}
                            {/*            </ListItem>*/}
                            {/*            <ListItem className={classes.customHoverFocus}*/}
                            {/*                      style={{border: '1px solid rgba(0, 0, 0, 0.1)', margin: "20px"}}*/}
                            {/*                      secondaryAction={*/}
                            {/*                          <React.Fragment>*/}
                            {/*                              <IconButton onClick={() => addToPlaylist()} edge="end"*/}
                            {/*                                          aria-label="Play">*/}
                            {/*                                  <PlaylistAddIcon/>*/}
                            {/*                              </IconButton>*/}
                            {/*                              <IconButton onClick={() => addToRecList()} edge="end" aria-label="add">*/}
                            {/*                                  <Add/>*/}
                            {/*                              </IconButton>*/}
                            {/*                          </React.Fragment>}>*/}
                            {/*                <ListItemAvatar>*/}
                            {/*                    <Avatar class='material-icons'>*/}
                            {/*                        <Looks3 className={classes.icons}/>*/}
                            {/*                    </Avatar>*/}
                            {/*                </ListItemAvatar>*/}
                            {/*                <ListItemText primary="Song Number 3" secondary="Artist Number 3"/>*/}
                            {/*            </ListItem>*/}
                            {/*        </List>*/}
                            {/*    </div>*/}
                            </div>
                            <div>
                                <div
                                    style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}
                                >
                                    <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>

                                    <div>
                                        <h3 style={{textAlign: 'center', color: "#1DB954"}}>My Recommendations</h3>
                                    </div>

                                    <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>
                                </div>
                                <div className="adding-song">
                                    {/*className={classes.root}*/}
                                    <Form.Control
                                        type="search"
                                        placeholder="Search Songs"
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
                                        {listRecs2}
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
                                {lyrics}
                            </div>
                        </React.Fragment>
                    )}

                </div>
            </Container>
        </div>
    )
}


//// Get an artist's top tracks
// spotifyApi.getArtistTopTracks('0oSGxfWSnnOXhD2fKuz2Gy', 'GB')
//   .then(function(data) {
//     console.log(data.body);
//     }, function(err) {
//     console.log('Something went wrong!', err);
//   });