import React, {useState, useEffect} from 'react'
import {View, Text} from 'react-native'
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

export default function Dashboard({sessionToken, nowPlaying, setNowPlaying}) {
    let darkModeOn = false;

    function getRandomInt(max) {
        return Math.floor(Math.random() * max);
    }

    const classes = useStyles()
    const [search, setSearch] = useState("")
    const [searchRecs, setSearchRecs] = useState("")
    const [searchResults, setSearchResults] = useState([])
    const [searchResultsRecommendation, setSearchResultsRecommendation] = useState([])
    const [playingTrack, setPlayingTrack] = useState()
    const [lyrics, setLyrics] = useState("")
    const [listRecommendation, setListRecommendations] = useState([])
    let searchArray = [];
    let searchRecsArray = [];

    const [list, setList] = useState(["First Recommendation", "Second Recommendation", "Third Recommendation"])
    const [list1, setList1] = useState([{trackName: ""}, {artistName: ""}, {isUsed: false}])
    let topSong1 = [{trackName: "Top Song"}, {artistName: "CS32"}, {isUsed: false}]
    let topSong2 = [{trackName: "Top Song 2"}, {artistName: "CS32"}, {isUsed: false}]
    let topSong3 = [{trackName: "Top Song 3"}, {artistName: "CS32"}, {isUsed: false}]

    const[topSongs, setTopSongs] = useState([topSong1, topSong2, topSong3])

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
            <ListItemText primary={song.trackName? song.trackName : "Song Title"} secondary={song.artistName? song.artistName : "Song Artist"} style={{color: "black"}}/>
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
                          <IconButton onClick={() => handleDelete(song.trackName)} edge="end" aria-label="delete">
                              <DeleteIcon style={{margin: "20px"}}/>
                          </IconButton>
                      </React.Fragment>
                  }>
            <ListItemAvatar>
                <Avatar sx={{ height: '60px', width: '60px', marginRight: "10px" }} alt="Album cover" src={song.albumArt? song.albumArt : SpotifyLogo}/>
            </ListItemAvatar>
            <ListItemText primary={song.trackName? song.trackName : "Song Title"} secondary={song.artistName? song.artistName : "Song Artist"} style={{color: "black"}}/>
        </ListItem>
    )

    function reRenderList(){
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
                              <IconButton onClick={() => handleDelete(song.trackName)} edge="end" aria-label="delete">
                                  <DeleteIcon style={{margin: "20px"}}/>
                              </IconButton>
                          </React.Fragment>
                      }>
                <ListItemAvatar>
                    <Avatar sx={{ height: '60px', width: '60px', marginRight: "10px" }} alt="Album cover" src={song.albumArt? song.albumArt : SpotifyLogo}/>
                </ListItemAvatar>
                <ListItemText primary={song.trackName? song.trackName : "Song Title"} secondary={song.artistName? song.artistName : "Song Artist"} style={{color: "black"}}/>
            </ListItem>
        )
        setListRecommendations(listRecs2)
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

            let tempList = list1

            if ((tempList[0].trackName === trackTitle && tempList[0].isUsed) || (tempList[1].trackName === trackTitle && tempList[1].isUsed) || (tempList[2].trackName === trackTitle && tempList[2].isUsed)) {
                console.log("can't repeat recommendations")
                console.log("list1[0].trackName: " + tempList[0].trackName)
                console.log("list1[1].trackName: " + tempList[1].trackName)
                console.log("list1[2].trackName: " + tempList[2].trackName)
            } else if (tempList[0].isUsed != true) {
                tempList[0].trackName = trackTitle;
                tempList[0].artistName = artist
                tempList[0].isUsed = true
                tempList[0].albumArt = track.albumUrl
                tempList[0].track = track
                console.log("added 1st rec")
                console.log("list1[0].isUsed : " + list1[0].isUsed)
                console.log("ID" + list1[0].id);
                // list1[0].albumURL = albumArt
            } else if (tempList[1].isUsed != true) {
                tempList[1].trackName = trackTitle;
                tempList[1].artistName = artist
                tempList[1].isUsed = true
                tempList[1].albumArt = track.albumUrl
                tempList[1].track = track
                console.log("added 2nd rec")

                // list1[1].albumURL = albumArt
            } else if (tempList[2].isUsed != true) {
                tempList[2].trackName = trackTitle;
                tempList[2].artistName = artist
                tempList[2].isUsed = true
                tempList[2].albumArt = track.albumUrl
                tempList[2].track = track
                console.log("added 3rd rec")

                // list1[2].albumURL =albumArt
            } else {
                console.log("Too many song recs")
            }
            setList1(tempList)
            // setListRecommendations(listRecs2)
            reRenderList()
        }
    }

    const handleDelete = (song) => {
        console.log("song.trackname" + song)
        // const newListRecs = listRecs2.filter((record) => record.trackName !== song)
        // const songToDelete = listRecs2.filter((record) => record.trackName === song)
        const newListRecs = listRecommendation.filter((record) => record.trackName !== song)
        const songToDelete = listRecommendation.filter((record) => record.trackName === song)

        console.log("songToDelete" + songToDelete)
        // console.log("listRecs2 b4 " + listRecs2[0].MuiListItemText)
        // listRecs2 = newListRecs

        // console.log("listRecs2 after" + listRecs2)
        // list1 = list1.filter((song) => song.id !== id) )
        let tempList = list1


        if (tempList[0].trackName == song) {
            console.log("songToDelete is in spot 0")
            tempList[0].trackName = "";
            tempList[0].artistName = "";
            tempList[0].isUsed = false;
            tempList[0].albumArt = SpotifyLogo;
        } else if (tempList[1].trackName == song) {
            console.log("songToDelete is in spot 1")
            tempList[1].trackName = "";
            tempList[1].artistName = "";
            tempList[1].isUsed = false;
            tempList[1].albumArt = SpotifyLogo;
        } else if (tempList[2].trackName == song) {
            console.log("songToDelete is in spot 2")
            tempList[2].trackName = "";
            tempList[2].artistName = "";
            tempList[2].isUsed = false;
            tempList[2].albumArt = SpotifyLogo;
        } else {
            console.log("deletion error")
        }
        setList1(tempList)
        setListRecommendations(newListRecs)
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

        // TODO: add to playlist endpoint
        // spotifyApi.addTracksToPlaylist('7xrIGuwvonS17zWjYzpUeR?si=665954b6a7874cf8', '["spotify:track:0xcl9XT60Siji6CSG4y6nb?si=9be52965d755468d"]').then(function (data) {
        //         console.log('Added tracks to playlist!');
        //     }, function (err) {
        //         console.log('Something went wrong!', err);
        //     });
    }


    useEffect(() => {
        if (!playingTrack) return
        const lyric = lyricsFinder(playingTrack.artist, playingTrack.title)
        setLyrics(lyric)
    }, [playingTrack])

    useEffect(() => {
        if (!search) return setSearchResults([])
        let cancel = false;
        const config = {
            headers: {
                'Authentication': sessionToken,
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            },
            params: {query: search}
        }
        axios.get(process.env.REACT_APP_SEARCH_ENDPOINT, config)
            .then(response => {
                console.log('search returned', response)
                if (cancel) return
                setSearchResults(response.data.map(track => {
                    return {
                        artist: track.artists[0].name,
                        title: track.name,
                        uri: track.uri,
                        albumUrl: track.album.images[0].url
                    }
                }))
            })
        return () => cancel = true
    }, [search, sessionToken])

    useEffect(() => {
        if (!searchRecs) return setSearchResultsRecommendation([])
        let cancel = false;

        const config = {
            headers: {
                'Authentication': sessionToken,
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            },
            params: {query: searchRecs}
        }

        axios.get(process.env.REACT_APP_SEARCH_ENDPOINT, config)
            .then(response => {
                if (cancel) return
                console.log('searchRecs returned', response)
                setSearchResultsRecommendation(response.data.map(track => {
                    return {
                        artist: track.artists[0].name,
                        title: track.name,
                        uri: track.uri,
                        albumUrl: track.album.images[0].url
                    }
                }))


            })
        return () => cancel = true
    }, [searchRecs, sessionToken])

    return (
        // darkModeOn ? 'black' : 'white'
        <div style={{background: darkModeOn ? 'black' : 'white'}}>
            <Container className="d-flex flex-column py-2" style={{height: "100vh"}}>
                <div className="top-bar">
                    <IconButton style={{ color: "#1DB954"}} edge="end"
                                aria-label="Home">
                        <HomeIcon/>
                    </IconButton>
                </div>
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
                                    <img src={playingTrack?.albumUrl}
                                         style={{height: "300px", width: "300px", border: '10px solid rgb(0, 0, 0)'}}/>
                                    <List sx={{width: '300px'}}>
                                        <ListItem className={classes.customHoverFocusNoMargin}
                                                  style={{border: '1px solid rgba(0, 0, 0, 0.1)'}}>
                                            <ListItemText primary={playingTrack?.title} secondary={playingTrack?.artist}/>
                                            <IconButton onClick={() => addToRecList(playingTrack)} edge="end"
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
                                        {listRecommendation}
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