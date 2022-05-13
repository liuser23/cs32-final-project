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
import {LooksOne} from "@material-ui/icons";

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
    const [playingTrack, setPlayingTrack] = useState()
    const [lyrics, setLyrics] = useState("")

    const [list1, setList1] = useState([{trackName: ""}, {artistName: ""}, {isUsed: false}])
    const [topSongs, setTopSongs] = useState([
        [{trackName: "Top Song"}, {artistName: "CS32"}, {isUsed: false}],
        [{trackName: "Top Song 2"}, {artistName: "CS32"}, {isUsed: false}],
        [{trackName: "Top Song 3"}, {artistName: "CS32"}, {isUsed: false}]
    ])

    function chooseTrack(track) {
        console.log("choosing + ", track)
        setPlayingTrack(track)
        setNowPlaying(track)
        setSearch("")
        setLyrics("")
    }

    const dummySong = {
        trackName: '',
        artistName: '',
        isUsed:  false,
        albumArt: SpotifyLogo,
        track: null,
    }

    const trackToSong = (track) => {
        return {
            trackName: track.name,
            artistName: track.artists[0].name,
            isUsed: true,
            albumArt: track.album.images[0].url,
            track: track,
        }
    }

    const addRecommendation = (track) => {
        setSearchRecs("")
        if (!track) {
            console.log('undefined track passed to add to rec list', track)
        }

        const songAlreadyPresent = list1.find((a) => a.isUsed && a.trackName === track.name)
        if (songAlreadyPresent !== undefined) {
            console.log('cant repeat recommendations')
            return
        }

        const insertIndex = list1.findIndex(a => !a.isUsed)
        if (insertIndex !== -1) {
            list1[insertIndex] = trackToSong(track)
            setList1([...list1])
        }
    }

    const deleteRecommendation = (song) => {
        const deletionIndex = list1.findIndex(a => a.trackName === song.trackName)
        if (deletionIndex !== -1) {
            list1[deletionIndex] = dummySong
            setList1([...list1])
        }
    }

    const addToPlaylist = () => {
        console.log("TODO: add playlist endpoint")
    }

    useEffect(() => {
        if (!playingTrack) return
        const lyric = 'TODO: lyrics error: The Same Origin Policy disallows reading the remote resource'
        // const lyric = lyricsFinder(playingTrack.artist, playingTrack.title)
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

    const topSongsList = topSongs.map((song) =>
        <ListItem key={song.trackName} alignItems="flex-start"
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
                          <IconButton onClick={() => deleteRecommendation(song)} edge="end" aria-label="delete">
                              <DeleteIcon style={{marginRight: "10px"}}/>
                          </IconButton>
                      </React.Fragment>
                  }>
            <ListItemAvatar>
                <Avatar className="material-icons">
                    <LooksOne className={classes.icons}/>
                </Avatar>
            </ListItemAvatar>
            <ListItemText primary={song.trackName? song.trackName : "Song Title"} secondary={song.artistName? song.artistName : "Song Artist"} style={{color: "black"}}/>
        </ListItem>
    )

    const listRecommendation = list1.map((song) =>
        <ListItem key={song.trackName} alignItems="flex-start"
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
                          <IconButton onClick={() => deleteRecommendation(song)} edge="end" aria-label="delete">
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
                                            <IconButton onClick={() => addRecommendation(playingTrack)} edge="end"
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
                                            chooseTrack={addRecommendation}
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