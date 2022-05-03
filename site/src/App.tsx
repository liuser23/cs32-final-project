import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import './App.css';
import Unauthenticated from "./routes/Unauthenticated";
import {
    BrowserRouter,
    Routes,
    Route, useSearchParams, useNavigate,
} from "react-router-dom";
import NewSession from "./routes/NewSession";
import PrivacySettings from "./routes/PrivacySettings";
import EditProfile from "./routes/EditProfile";
import ChangePassword from "./routes/ChangePassword";
import NotificationSettings from "./routes/NotificationSettings";
import FriendsList from "./routes/FriendsList";
import MyRecommendations from "./routes/MyRecommendations";
import SignOut from "./routes/SignOut";
import Home from "./routes/Home";


function App() {
    const [sessionToken, setSessionToken] = useState<string>('')
    return (
      <BrowserRouter>
          <Routes>
              <Route path={"/"} element={<Authenticate sessionToken={sessionToken}><Home sessionToken={sessionToken} /></Authenticate>}/>
              <Route path={"newSession"} element={<NewSession setSessionToken={setSessionToken}/>}/>
              <Route path={"privacysettings"} element={<Authenticate sessionToken={sessionToken}><PrivacySettings/></Authenticate>}/>
              <Route path={"editprofile"} element={<Authenticate sessionToken={sessionToken}><EditProfile/></Authenticate>}/>
              <Route path={"changepassword"} element={<Authenticate sessionToken={sessionToken}><ChangePassword/></Authenticate>}/>
              <Route path={"notificationsettings"} element={<Authenticate sessionToken={sessionToken}><NotificationSettings/></Authenticate>}/>
              <Route path={"FriendsList"} element={<Authenticate sessionToken={sessionToken}><FriendsList/></Authenticate>}/>
              <Route path={"myrecommendations"} element={<Authenticate sessionToken={sessionToken}><MyRecommendations/></Authenticate>}/>
              <Route path={"signout"} element={<Authenticate sessionToken={sessionToken}><SignOut/></Authenticate>}/>
          </Routes>
      </BrowserRouter>
  )
}

function Authenticate(props: {sessionToken: string | undefined, children: JSX.Element}) {
    if (props.sessionToken) {
        return props.children
    } else {
        return <Unauthenticated />
    }
}

export default App;
