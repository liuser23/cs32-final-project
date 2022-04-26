import React, { useState, useEffect } from 'react';
import WebPlayback from './WebPlayback'
import Login from './Login'
import Dashboard from "./Dashboard"
import './App.css';
import SearchBar from "./SearchBar";

// const code = new URLSearchParams(window.location.search).get("code")
//
// function App() {
//   return code ? <Dashboard code={code} /> : <Login />
// }
//
// export default App

function App() {

  const [token, setToken] = useState('');

  useEffect(() => {

    async function getToken() {
      const response = await fetch('/auth/token');
      const json = await response.json();
      setToken(json.access_token);
    }

    getToken();

  }, []);

  return (
    <>
        { (token === '') ? <Login/> : <WebPlayback token={token} /> }
      {/*return code ? <Dashboard code={code} /> : <Login />*/}
      <SearchBar/>
    </>

  );
}


export default App;
