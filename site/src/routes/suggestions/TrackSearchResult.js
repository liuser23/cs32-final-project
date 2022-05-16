import React from 'react'

export default function TrackSearchResult({track, chooseTrack, isRec}) {
    function handlePlay() {
        chooseTrack(track)
    }

    return (
        <div id={"songResult"} className="d-flex m-2 align-items-center"
             style={{cursor: "pointer"}}
            onClick={handlePlay}
            >

            <img src={track.album.images[0].url} style={{height: "64px", width: "64px"}}/>
            <div id={"songInfo"} className="ml-3">
                <div id={"songName"}>{track.name}</div>
                <div id={"songArtist"} className="text-muted">{track.artists[0].name}</div>
            </div>
        </div>
    )
}