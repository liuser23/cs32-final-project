import React, {useEffect, useState} from 'react';
import '../App.css';
import SideBar from "../SideBar";
import DefaultPfp from "../images/PngItem_1503945.png";
import {SidebarConfig} from "../App";
import Button from "@mui/material/Button";

function PrivacySettings() {

    return (
        <div className={"Empty-Background"} style={{flex: 1, padding: '370px'}}>
            <div >
                <Button variant="outlined" color="success" size="large" className="Empty-Button" type='submit' >Settings</Button>
            </div>
        </div>
    )
}

export default PrivacySettings;