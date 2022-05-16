import React, {useEffect, useState} from 'react';
import '../App.css';
import Button from "@mui/material/Button";

import SideBar from "../SideBar";
import DefaultPfp from "../images/PngItem_1503945.png";
import {SidebarConfig} from "../App";
// import axios from 'axios'
// import List from '@mui/material/List';
// import ListItem from '@mui/material/ListItem';
// import ListItemText from '@mui/material/ListItemText';
// import ListItemAvatar from '@mui/material/ListItemAvatar';
// import Avatar from '@mui/material/Avatar';
// import {makeStyles} from "@material-ui/core/styles";
// import SpotifyLogo from './spotify-logo.png'
// import PlayCircleOutlineIcon from '@material-ui/icons/PlayCircleOutline'
// import {Button, IconButton} from '@material-ui/core'
// import DeleteIcon from '@material-ui/icons/Delete';
// import PlaylistAddIcon from "@mui/icons-material/PlaylistAdd";
//
// const useStyles = makeStyles((theme) => ({
//     customHoverFocus: {
//         margin: theme.spacing(1),
//         "&:hover, &.Mui-focusVisible": {backgroundColor: "#1DB954"}
//     },
//     customHoverFocusNoMargin: {
//         "&:hover, &.Mui-focusVisible": {backgroundColor: "#1DB954"}
//     },
// }))
//
// export default function EditProfile({sessionToken, nowPlaying, setNowPlaying}) {
//     const classes = useStyles()
//     const [myRecs, setMyRecs] = useState([])
//     const [reloadSuggestions, setReloadSuggestions] = useState(0)
//
//     function chooseTrack(track) {
//         console.log("choosing + ", track)
//         setNowPlaying(track)
//     }
//
//     const deleteRecommendation = (track) => {
//         const data = { songId: nowPlaying.id, suggestion: track.id }
//         const config = {
//             headers: {
//                 'Authentication': sessionToken,
//                 "Content-Type": "application/json",
//                 'Access-Control-Allow-Origin': '*',
//             }
//         }
//         axios.post(process.env.REACT_APP_DELETE_SUGGESTIONS, data, config)
//             .then(response => {
//                 setReloadSuggestions(Math.random())
//                 console.log('delete', response)
//             })
//     }
//
//
//     useEffect(() => {
//         if (!nowPlaying) return
//         // const lyric = 'TODO: lyrics error: The Same Origin Policy disallows reading the remote resource'
//         // // const lyric = lyricsFinder(nowPlaying.artist, nowPlaying.title)
//         // setLyrics(lyric)
//         axios
//             .get("http://localhost:3000/lyrics", {
//                 params: {
//                     track: nowPlaying.title,
//                     artist: nowPlaying.artist,
//                 },
//             })
//             .then(res => {
//                 console.log(res.data.lyrics)
//             })
//     }, [nowPlaying])
//
//
//     useEffect(() => {
//         if (!nowPlaying) return
//         const data = { songId: nowPlaying.id }
//         const config = {
//             headers: {
//                 'Authentication': sessionToken,
//                 "Content-Type": "application/json",
//                 'Access-Control-Allow-Origin': '*',
//             },
//             params: {query: nowPlaying.id}
//         }
//         axios.get(process.env.REACT_APP_USER_SUGGESTIONS, config)
//             .then(response => {
//                 console.log('setting user suggestions', response.data)
//                 setMyRecs(response.data)
//             })
//     }, [nowPlaying, setMyRecs, reloadSuggestions])
//
//     return (
//         <div className={"Empty-Background"} style={{flex: 1, padding: '370px'}}>
//             <div >
//                 <Button variant="outlined" size="large" className="Empty-Button" type='submit' >See My Recommendations</Button>
//             </div>
//             <div>
//                 <List sx={{width: '450px', padding: "10px"}}>
//
//                 {myRecs.map(song =>
//                 <ListItem key={song.id} alignItems="flex-start"
//                           style={{margin: "8px", padding: "20px", border: '1px solid rgba(0, 0, 0, 0.1)'}}
//                           className={classes.customHoverFocus}
//                           secondaryAction={
//                               <React.Fragment>
//                                   <IconButton onClick={() => chooseTrack(song)} edge="end" aria-label="play">
//                                       <PlayCircleOutlineIcon style={{margin: "10px"}}/>
//                                   </IconButton>
//                                   <IconButton  edge="end" aria-label="Play">
//                                       <PlaylistAddIcon/>
//                                   </IconButton>
//                                   <IconButton onClick={() => deleteRecommendation(song)} edge="end" aria-label="delete">
//                                       <DeleteIcon style={{margin: "20px"}}/>
//                                   </IconButton>
//                               </React.Fragment>
//                           }>
//                     <ListItemAvatar>
//                         <Avatar sx={{ height: '60px', width: '60px', marginRight: "10px" }} alt="Album cover" src={song?.album.images[0].url? song?.album.images[0].url : SpotifyLogo}/>
//                     </ListItemAvatar>
//                     <ListItemText primary={song?.name? song?.name : "Song Title"} secondary={song?.artists[0].name? song?.artists[0].name : "Song Artist"} style={{color: "black"}}/>
//                 </ListItem>
//                 )}
//                 </List>
//
//             </div>
//         </div>
//     )
// }

function EditProfile() {

    return (
        <div id="emptyBackground" className={"Empty-Background"} style={{flex: 1, padding: '370px'}}>
            <div >
                <Button variant="outlined" color="success" size="large" className="Empty-Button" type='submit' >My Recs</Button>
            </div>
        </div>
    )
}

export default EditProfile;
