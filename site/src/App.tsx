import React from 'react';
import './App.css';

const LOGIN_ENDPOINT = '/login'

function App() {
  const clicked = () => {
    console.log('clicked');
  };

  return <>
    <a href={LOGIN_ENDPOINT}>Login in to Spotify</a>
    <button onClick={clicked}>Click me</button>
  </>
}

export default App
