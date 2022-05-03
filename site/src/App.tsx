import React from 'react';
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
              <Route path={"privacysettings"} element={<PrivacySettings/>}/>
              <Route path={"editprofile"} element={<EditProfile/>}/>
              <Route path={"changepassword"} element={<ChangePassword/>}/>
              <Route path={"notificationsettings"} element={<NotificationSettings/>}/>
              <Route path={"FriendsList"} element={<FriendsList/>}/>
              <Route path={"myrecommendations"} element={<MyRecommendations/>}/>
              <Route path={"signout"} element={<SignOut/>}/>
          </Routes>
      </BrowserRouter>
  )
}

export default App;
