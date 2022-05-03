import React, {useState} from 'react';
import './App.css';
import Landing from "./Landing";
import {
    BrowserRouter,
    Routes,
    Route,
} from "react-router-dom";
import Authorized from "./routes/Authorized";
import PrivacySettings from "./routes/PrivacySettings";
import EditProfile from "./routes/EditProfile";
import ChangePassword from "./routes/ChangePassword";
import NotificationSettings from "./routes/NotificationSettings";
import FriendsList from "./routes/FriendsList";
import MyRecommendations from "./routes/MyRecommendations";
import SignOut from "./routes/SignOut";
import DefaultPfp from "./images/PngItem_1503945.png";

//const LOGIN_ENDPOINT = '/login'

function App() {
    /*
  const clicked = () => {
    console.log('clicked');
  };
     */

    const [pfp, setPfp] = useState<string>(DefaultPfp);

  return (
      <BrowserRouter>
          <Routes>
              <Route path={"/"} element={<Landing/>}/>
              <Route path={"authorized"} element={<Authorized setPfp={setPfp} userPfp={pfp}/>}/>
              <Route path={"privacysettings"} element={<PrivacySettings userPfp={pfp}/>}/>
              <Route path={"editprofile"} element={<EditProfile userPfp={pfp}/>}/>
              <Route path={"changepassword"} element={<ChangePassword userPfp={pfp}/>}/>
              <Route path={"notificationsettings"} element={<NotificationSettings userPfp={pfp}/>}/>
              <Route path={"FriendsList"} element={<FriendsList userPfp={pfp}/>}/>
              <Route path={"myrecommendations"} element={<MyRecommendations userPfp={pfp}/>}/>
              <Route path={"signout"} element={<SignOut userPfp={pfp}/>}/>
          </Routes>
      </BrowserRouter>
  )
}

export default App;
