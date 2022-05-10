import React, { useState, useEffect} from 'react'
import {View, Text} from 'react-native'
import useAuth from './useAuth'
import {Container, Form} from 'react-bootstrap'
import SpotifyWebApi from "spotify-web-api-node"
import TrackSearchResult from "./TrackSearchResult";
import Player from "./Player";
import axios from 'axios'
import Recommendations from "./Recommendations";
import TodoList from './TodoList';
import FormRecs from './Form'
import {v4 as uuidv4} from 'uuid'
import TextField from '@material-ui/core/TextField';
import {makeStyles} from "@material-ui/core/styles";
import {Button, IconButton} from '@material-ui/core'
import DeleteIcon from '@material-ui/icons/Delete';
import Add from '@material-ui/icons/Add'
// import { makeStyles } from '@material-ui/core/styles';

//import DeleteIcon from '@material-ui/core/IconButton'
// import Delete from '@mui/icons-material/'
import AddIcon from '@material-ui/core/IconButton'
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
        "&:hover, &.Mui-focusVisible": { backgroundColor: "#1DB954" }
    },
    viewStyleForLine: {
        borderBottomColor: "black",
        borderBottomWidth: StyleSheet.hairlineWidth,
        alignSelf:'stretch',
        width: "100%"
    }
}))

export default function Dashboard({code}) {
    const classes = useStyles()
    const accessToken = useAuth(code)
    const [search, setSearch] = useState("")
    const [searchRecs, setSearchRecs] = useState("")
    const [searchResults, setSearchResults] = useState([])
    const [searchResultsRecommendation, setSearchResultsRecommendation] = useState([])
    const [playingTrack, setPlayingTrack] = useState()
    const [lyrics, setLyrics] = useState("")
    const [recommendations, setRecommendation] = useState("")
    let searchArray = [];
    let searchRecsArray = [];
    let updatedRecs = ["hello"]


    function chooseTrack(track) {
        setPlayingTrack(track)
        setSearch("")
        setLyrics("")
    }

    function addToRecList(track) {

        // updatedRecs.map((track=>
        //     <li>track.title</li>));
        //
        // console.log("HELLO!")
    }

    function exampleAdd() {
        // console.log("HELLO")
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
                const smallestAlbumImage = track.album.images.reduce((smallest, image) => {
                    if (image.height < smallest.height) return image
                    return smallest
                }, track.album.images[0])
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

    useEffect(() => {
        if (!searchRecs) return setSearchResultsRecommendation([])
        if (!accessToken) return
        let cancel = false;
        spotifyApi.searchTracks(searchRecs).then(res => {
            if (cancel) return
            searchRecsArray = res.body.tracks.items;
            console.log("search array: " + searchRecsArray)
            setSearchResultsRecommendation(res.body.tracks.items.map(track => {
                const smallestAlbumImage = track.album.images.reduce((smallest, image) => {
                    if (image.height < smallest.height) return image
                    return smallest
                }, track.album.images[0])
                return {
                    artist: track.artists[0].name,
                    title: track.name,
                    uri: track.uri,
                    albumUrl: smallestAlbumImage.url
                }
            }))
        })

        return () => cancel = true
    }, [searchRecs, accessToken])

    return (
        <Container className="d-flex flex-column py-2" style={{height: "100vh"}}>
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
                            <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}} />

                            <div>
                                <h3 style={{textAlign: 'center', color: "#1DB954"}}>Recommendations</h3>
                            </div>

                            <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}} />
                        </div>
                        <div>
                            <Recommendations searchArray={searchArray}/>
                        </div>
                        <div>
                            {/*<div className = "my-rec">*/}
                            {/*    <hr*/}
                            {/*        style={{*/}
                            {/*            color: "#1DB954",*/}
                            {/*            width: "100",*/}
                            {/*            backgroundColor: "#1DB954",*/}
                            {/*            height: 5*/}
                            {/*        }}*/}
                            {/*    />*/}
                            {/*</div>*/}
                            <div
                                style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}
                            >
                                <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}} />

                                <div>
                                    <h3 style={{textAlign: 'center', color: "#1DB954"}}>My Recommendations</h3>
                                </div>

                                <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}} />
                            </div>
                            <div className="adding-song">
                                 {/*className={classes.root}*/}
                            <form className={classes.customHoverFocus} >
                                <TextField
                                    name="song-rec"
                                    label="Add Song"
                                    value={searchRecs}
                                    variant="filled"
                                    onChange={e => setSearchRecs(e.target.value)}
                                />
                            </form>

                                {/*<IconButton variant="contained" aria-label="delete" color="black" className={classes.margin}>*/}
                                {/*    <DeleteIcon />*/}
                                {/*     Delete*/}
                                {/*</IconButton>*/}
                                <Button
                                    variant="contained"
                                    color="black"
                                    className={classes.customHoverFocus}
                                    startIcon={<Add />}
                                >
                                    Add
                                </Button>
                                <Button
                                    variant="contained"
                                    color="black"
                                    className={classes.customHoverFocus}
                                    startIcon={<DeleteIcon />}
                                >
                                    Delete
                                </Button>


                                {/*<Button variant="outlined" startIcon={<Delete/>}>*/}
                                {/*    Delete*/}
                                {/*</Button>*/}


                                {/*type="searchRecs"*/}
                                {/*placeholder="Add Song Recommendation"*/}
                                {/*value={searchRecs}*/}


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
                            {/*<div className="recsList">*/}
                            {/*    <ul>*/}
                            {/*        {updatedRecs}*/}
                            {/*    </ul>*/}
                            {/*</div>*/}
                        </div>
                        {/*<div>*/}
                        {/*    <TodoList*/}
                        {/*        code={code}*/}
                        {/*    />*/}
                        {/*</div>*/}
                        <div className="text-center" style={{whiteSpace: "pre"}}>
                            <div
                                style={{display: 'flex', flexDirection: 'row', alignItems: 'center'}}
                            >
                                <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}} />

                                <div>
                                    <h3 style={{textAlign: 'center', color: "#1DB954"}}>Lyrics</h3>
                                </div>

                                <div style={{flex: 1, height: '1px', backgroundColor: "#1DB954"}} />
                            </div>
                            {lyrics}
                        </div>
                    </React.Fragment>
                )}

            </div>
            <div>
                <Player accessToken={accessToken} trackUri={playingTrack?.uri}/>
            </div>
        </Container>
    )
}


//// Get an artist's top tracks
// spotifyApi.getArtistTopTracks('0oSGxfWSnnOXhD2fKuz2Gy', 'GB')
//   .then(function(data) {
//     console.log(data.body);
//     }, function(err) {
//     console.log('Something went wrong!', err);
//   });