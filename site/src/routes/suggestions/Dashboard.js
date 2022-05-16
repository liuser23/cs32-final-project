import React, {useState, useEffect} from 'react'
import {Container, Form} from 'react-bootstrap'
import TrackSearchResult from "./TrackSearchResult";
import axios from 'axios'
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import {makeStyles} from "@material-ui/core/styles";
import {Button, IconButton} from '@material-ui/core'
import DeleteIcon from '@material-ui/icons/Delete';
import Add from '@material-ui/icons/Add'
import PlayCircleOutlineIcon from '@material-ui/icons/PlayCircleOutline'
import SpotifyLogo from './spotify-logo.png'
import HomeIcon from '@material-ui/icons/Home'
import PlaylistAddIcon from '@mui/icons-material/PlaylistAdd';
import HelpOutlineIcon from '@material-ui/icons/HelpOutline';
import '../../App.css';
import {LooksOne, LooksTwo, Looks3} from "@material-ui/icons";



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
    const classes = useStyles()
    const [search, setSearch] = useState("")
    const [searchRecs, setSearchRecs] = useState("")
    const [searchResults, setSearchResults] = useState([])
    const [searchResultsRecommendation, setSearchResultsRecommendation] = useState([])
    const [lyrics, setLyrics] = useState("")
    const [list1, setList1] = useState([])
    const [reloadSuggestions, setReloadSuggestions] = useState(0)
    const [topSongs, setTopSongs] = useState([])

    function chooseTrack(track) {
        console.log("choosing + ", track)
        setNowPlaying(track)
        setSearch("")
        setLyrics("")
    }

    const addRecommendation = (track) => {
        console.log('running')
        setSearchRecs("")
        if (!track) {
            console.log('undefined track passed to add to rec list', track)
        }
        const data = { songId: nowPlaying.id, suggestion: track.id }
        const config = {
            headers: {
                'Authentication': sessionToken,
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.post(process.env.REACT_APP_INSERT_SUGGESTIONS, data, config)
            .then(response => {
                setReloadSuggestions(Math.random())
                console.log('insert', response)
            })
    }

    const deleteRecommendation = (track) => {
        const data = { songId: nowPlaying.id, suggestion: track.id }
        const config = {
            headers: {
                'Authentication': sessionToken,
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        axios.post(process.env.REACT_APP_DELETE_SUGGESTIONS, data, config)
            .then(response => {
                setReloadSuggestions(Math.random())
                console.log('delete', response)
            })
    }



    const addToPlaylist = () => {
        console.log("TODO: add playlist endpoint")
    }

    useEffect(() => {
        if (!nowPlaying) return
        const config = {
            headers: {
                'Authentication': sessionToken,
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            },
            params: {query: nowPlaying.id}
        }
        axios.get(process.env.REACT_APP_USER_SUGGESTIONS, config)
            .then(response => {
                console.log('setting user suggestions', response.data)
                setList1(response.data)
            })
    }, [nowPlaying, setList1, reloadSuggestions])

    useEffect(() => {
        if (!nowPlaying) {
            setTopSongs([])
            return
        }
        const config = {
            headers: {
                'Authentication': sessionToken,
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            },
            params: {query: nowPlaying.id}
        }
        axios.get(process.env.REACT_APP_TOP_SUGGESTIONS, config)
            .then(response => {
                console.log('setting top songs', response.data)
                setTopSongsIcon(response.data)
                // setTopSongs(response.data)
            })
    }, [nowPlaying, setTopSongs, reloadSuggestions])

    function setTopSongsIcon(songs) {
        for (let i = 0; i < songs.length; i++){
            songs[i].place = i;
        }
        setTopSongs(songs)
    }

    useEffect(() => {
        if (!nowPlaying) return
        // const lyric = 'TODO: lyrics error: The Same Origin Policy disallows reading the remote resource'
        // // const lyric = lyricsFinder(nowPlaying.artist, nowPlaying.title)
        // setLyrics(lyric)
        axios
            .get("http://localhost:3000/lyrics", {
                params: {
                    track: nowPlaying.title,
                    artist: nowPlaying.artist,
                },
            })
            .then(res => {
                setLyrics(res.data.lyrics)
            })
    }, [nowPlaying])

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
                setSearchResults(response.data)
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
                setSearchResultsRecommendation(response.data)
            })
        return () => cancel = true
    }, [searchRecs, sessionToken])

    const topSongsList = topSongs.map(song =>
        <ListItem key={song.id} alignItems="flex-start"
                  style={{margin: "8px", padding: "20px", border: '1px solid rgba(0, 0, 0, 0.1)'}}
                  className={classes.customHoverFocus}
                  secondaryAction={
                      <div id={"topSongsButtons"}>
                          <IconButton id="playSongButton" onClick={() => chooseTrack(song)} edge="end" aria-label="play">
                              <PlayCircleOutlineIcon style={{marginLeft: "10px"}}/>
                          </IconButton>
                          <IconButton id="playlistButton" onClick={() => addToPlaylist()} edge="end" aria-label="Play">
                              <PlaylistAddIcon/>
                          </IconButton>
                      </div>
                  }>
            <ListItemAvatar>
                <Avatar class="material-icons">
                    {song?.place === 0 &&  <LooksOne id="number-icon" alt="Number One Icon" className={classes.icons}/>}
                    {song?.place === 1 &&  <LooksTwo id="number-icon" alt="Number Two Icon" className={classes.icons}/>}
                    {song?.place === 2 &&  <Looks3 id="number-icon" alt="Number Three Icon" className={classes.icons}/>}
                    {song?.place === -1 && <HelpOutlineIcon className={classes.icons}/>}
                    {song?.place > 3 && <HelpOutlineIcon className={classes.icons}/>}

                </Avatar>
            </ListItemAvatar>
            <ListItemText primary={song?.name? song?.name : "Song Title"} secondary={song?.artists[0].name? song?.artists[0].name : "Song Artist"} style={{color: "black"}}/>
        </ListItem>
    )

    const listRecommendation = list1.map(song =>
        <ListItem key={song.id} alignItems="flex-start"
                  style={{margin: "8px", padding: "20px", border: '1px solid rgba(0, 0, 0, 0.1)'}}
                  className={classes.customHoverFocus}
                  secondaryAction={
                      <div id={"songRecsButtons"}>
                          <IconButton id="playSongButton" onClick={() => chooseTrack(song)} edge="end" aria-label="play">
                              <PlayCircleOutlineIcon style={{margin: "10px"}}/>
                          </IconButton>
                          <IconButton id="playlistButton" onClick={() => addToPlaylist()} edge="end" aria-label="Play">
                              <PlaylistAddIcon/>
                          </IconButton>
                          <IconButton id="deleteRecButton" onClick={() => deleteRecommendation(song)} edge="end" aria-label="delete">
                              <DeleteIcon style={{margin: "20px"}}/>
                          </IconButton>
                      </div>
                  }>
            <ListItemAvatar>
                <Avatar sx={{ height: '60px', width: '60px', marginRight: "10px" }} alt="Album cover" src={song?.album.images[0].url? song?.album.images[0].url : SpotifyLogo}/>
            </ListItemAvatar>
            <ListItemText primary={song?.name? song?.name : "Song Title"} secondary={song?.artists[0].name? song?.artists[0].name : "Song Artist"} style={{color: "black"}}/>
        </ListItem>
    )

    return (
        // darkModeOn ? 'black' : 'white'
        <div id="mainWindow" className={"Main-window"} style={{background: darkModeOn ? 'black' : 'white'}}>
            <Container id="mainDiv" className="d-flex flex-column py-2" style={{height: "100vh"}}>
                {/*<div className="top-bar">*/}
                {/*    <IconButton style={{ color: "#1DB954"}} edge="end"*/}
                {/*                aria-label="Home">*/}
                {/*        <HomeIcon/>*/}
                {/*    </IconButton>*/}
                {/*</div>*/}
                <div id={"findSongRecsBar"}
                    style={{display: 'flex', flexDirection: 'row', alignItems: 'center', paddingTop: "30px"}}
                >
                    <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>

                    <div id={"findSongRecsBarHeader"}>
                        <h3 style={{textAlign: 'center', color: "#1DB954"}}>Find Song Recommendations</h3>
                    </div>

                    <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>
                </div>
                <div id="searchBarDiv" className={'adding-song'} >
                    <Form.Control id="searchBarForm" className={'adding-song'}
                        size='lg'
                        type="text"
                        placeholder="Search Songs/Albums"
                        value={search}
                        onChange={e => setSearch(e.target.value)}
                    />

                </div>
                <div id={"searchResultsFindSongDiv"} className="flex-grow-1 my-2" style={{overflowY: "auto", width: '100%'}}>
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
                            <div id={"topRecsBar"}
                                style={{display: 'flex', flexDirection: 'row', alignItems: 'center', paddingTop: "30px"}}
                            >
                                <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>

                                <div id={"topRecsBarHeader"}>
                                    <h3 style={{textAlign: 'center', color: "#1DB954"}}>Top Recommendations</h3>
                                </div>

                                <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>
                            </div>
                            <div id={"playing-and-top-recs"} className="playing-and-top-recs">
                                <div id="currently-playing" className="currently-playing">
                                    <img src={nowPlaying?.album.images[0].url}
                                         style={{height: "300px", width: "300px", border: '10px solid rgb(0, 0, 0)'}}/>
                                    <List id="list" sx={{width: '300px'}}>
                                        <ListItem id="listItem" className={classes.customHoverFocusNoMargin}
                                                  style={{border: '1px solid rgba(0, 0, 0, 0.1)'}}>
                                            <ListItemText id="listItemText" primary={nowPlaying?.name} secondary={nowPlaying?.artists[0].name}/>

                                            <IconButton onClick={() => addToPlaylist()} edge="end" aria-label="Play">
                                                <PlaylistAddIcon/>
                                            </IconButton>
                                        </ListItem>
                                    </List>
                                </div>
                                <div id="topSongsList" className="topSongsList">
                                    <List id="topSongsList" sx={{width: '450px', padding: "10px"}}>
                                        {topSongsList}
                                    </List>
                                </div>
                            </div>
                            <div id={"topRecsAndSearchBarDiv"}>
                                <div id={"makeRecsBar"}
                                    style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}
                                >
                                    <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>

                                    <div id={"makeRecsBarHeader"}>
                                        <h3 style={{textAlign: 'center', color: "#1DB954"}}>Make Recommendations</h3>
                                    </div>

                                    <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>
                                </div>
                                <div  id="addingSong" className="adding-song">
                                    {/*className={classes.root}*/}
                                    <Form.Control id="searchRecForm" className={'adding-song'}
                                        size="lg"
                                        type="text"
                                        placeholder="Search Songs to Recommend"
                                        value={searchRecs}
                                        onChange={e => setSearchRecs(e.target.value)}
                                    />
                                </div>
                                <div id={"searchResultsAddRecDiv"} className="flex-grow-1 my-2" style={{overflowY: "auto"}}>
                                    {searchResultsRecommendation.map(track => (
                                        <TrackSearchResult
                                            track={track}
                                            key={track.uri}
                                            chooseTrack={addRecommendation}
                                            isRec={true}
                                        />
                                    ))}
                                </div>
                                <div id={"recsList"} className="recsList">
                                    <List sx={{width: '100%', padding: "10px"}}>
                                        {listRecommendation}
                                    </List>
                                </div>
                            </div>
                            <div id={"lyricsBarDiv"} className="text-center" style={{whiteSpace: "pre"}}>
                                <div id={"lyricsBar"}
                                    style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}
                                >
                                    <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}}/>

                                    <div id={"lyricsBarHeader"}>
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