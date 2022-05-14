import React from 'react'
import './App.css'

export default function RecommendationsForm({}) {

    function setSearch() {

    }

    return (
        <div className = "recommendationForm">
            <h1>My Recommendations</h1>
            <form>
                    <input type="text" placeholder="Enter Song Recommendation" size="50" onChange={setSearch}/><br/><br/>
                    <input type="text" placeholder="Enter Song Recommendation" size="50"/><br/><br/>
                    <input type="text" placeholder="Enter Song Recommendation" size="50"/><br/><br/>
            </form>
        </div>
    )
}