import React, {useEffect, useState} from 'react';
import './App.css';
import {Link} from "react-router-dom";

function AccMenuButton(props : {picture : string, picAlt : string, txtContent : string, route : string}) {

    return (
        <Link className={"Accmenu-button"} to={props.route}>
            <img className={"Button-img"} src={props.picture} alt={props.picAlt}/>
            <p className={"Button-text"}>{props.txtContent}</p>
        </Link>
    )
}

export default AccMenuButton;