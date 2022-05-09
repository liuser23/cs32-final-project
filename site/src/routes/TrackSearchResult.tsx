import React, {Dispatch, SetStateAction} from 'react'
import {SearchResult} from "./Search";

export default function TrackSearchResult(props: {key: string, track: SearchResult, setNowPlaying: Dispatch<SetStateAction<string | undefined>>}) {
    return (
        <div className="d-flex m-2 align-items-center"
             style={{cursor: "pointer"}}
            onClick={() => props.setNowPlaying(props.track.spotifyId)}>
            <img
                src={props.track.image}
                style={{height: "64px", width: "64px"}}
                alt={`${props.track.name}'s album cover`}/>

            <div className="ml-3">
                <div>{props.track.name}</div>
                <div className="text-muted">{props.track.artist}</div>
            </div>
        </div>
    )
}