import React from 'react';
import './App.css';
import Landing from "./Landing";
import {
    BrowserRouter,
    Routes,
    Route,
} from "react-router-dom";
import Authorized from "./routes/Authorized";

//const LOGIN_ENDPOINT = '/login'

function App() {
    /*
  const clicked = () => {
    console.log('clicked');
  };
     */

  return (
      <BrowserRouter>
          <Routes>
              <Route path={"/"} element={<Landing/>}/>
              <Route path={"authorized"} element={<Authorized/>}/>
          </Routes>
      </BrowserRouter>
  )
}

export default App;
