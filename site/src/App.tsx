import React, {useState} from 'react';
import './App.css';
import {
    BrowserRouter,
    Routes,
    Route,
} from "react-router-dom";
import PrivacySettings from "./routes/PrivacySettings";
import EditProfile from "./routes/EditProfile";
import ChangePassword from "./routes/ChangePassword";
import NotificationSettings from "./routes/NotificationSettings";
import FriendsList from "./routes/FriendsList";
import MyRecommendations from "./routes/MyRecommendations";
import SignOut from "./routes/SignOut";
import DefaultPfp from "./images/PngItem_1503945.png";
import Home from "./routes/Home";
import NewSession from "./routes/NewSession";
import Unauthenticated from "./routes/Unauthenticated";
import SideBar from "./SideBar";

type SidebarConfig = {
    profilePicturePath: string,
}

function App() {
    const [sessionToken, setSessionToken] = useState<string>('')
    const [sidebarConfig, setSidebarConfig] = useState<SidebarConfig>({
        profilePicturePath: DefaultPfp,
    })

    if (sessionToken) {
        return (
            <BrowserRouter>
                <SideBar sidebarConfig={sidebarConfig}/>
                <Routes>
                    <Route path={"/"} element={<Home sessionToken={sessionToken} setSidebarConfig={setSidebarConfig}/>}/>
                    <Route path={"privacysettings"} element={<PrivacySettings/>}/>
                    <Route path={"editprofile"} element={<EditProfile/>}/>
                    <Route path={"changepassword"} element={<ChangePassword/> }/>
                    <Route path={"notificationsettings"} element={<NotificationSettings/>}/>
                    <Route path={"FriendsList"} element={<FriendsList/>}/>
                    <Route path={"myrecommendations"} element={<MyRecommendations/>}/>
                    <Route path={"signout"} element={<SignOut/>}/>
                </Routes>
            </BrowserRouter>
        )
    } else {
        return (
            <BrowserRouter>
                <Routes>
                    <Route path={"/"} element={<Unauthenticated />}/>
                    <Route path={"newSession"} element={<NewSession setSessionToken={setSessionToken}/>}/>
                </Routes>
            </BrowserRouter>
        )

    }
}

export type { SidebarConfig }
export default App
