import React from 'react'

export default function TrackSearchResult({track, chooseTrack, isRec}) {
    function handlePlay() {
        chooseTrack(track)
    }

    return (
        <div className="d-flex m-2 align-items-center"
             style={{cursor: "pointer"}}
            onClick={handlePlay}
            >

            <img src={track.album.images[0].url} style={{height: "64px", width: "64px"}}/>
            <div className="ml-3">
                <div>{track.name}</div>
                <div className="text-muted">{track.artists[0].name}</div>
            </div>
        </div>
    )
}