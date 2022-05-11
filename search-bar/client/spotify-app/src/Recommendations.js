import React from 'react'

export default function Recommendations({searchArray}) {

    return (
        <React.Fragment>
            <div className="d-flex justify-content-center align-items-center">
                <h1>Recommendations</h1>
            </div>
            <div className="d-flex justify-content-center align-items-center">

                <ol>
                    <li>Rec 1</li>
                    <li>Rec 2</li>
                    <li>Rec 3</li>
                </ol>
            </div>
        </React.Fragment>
    )
}