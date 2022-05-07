import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import '../App.css';
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {Authentication} from "../App";

function NewSession(props: {setAuthentication: Dispatch<SetStateAction<Authentication | undefined>>}) {
    const parameters = new URLSearchParams(useLocation().search)
    const sessionToken = parameters.get('sessionToken')
    const accessToken = parameters.get('accessToken')
    const navigate = useNavigate()

    useEffect(() => {
        console.log(`sessionToken: ${sessionToken} accessToken: ${accessToken}`)
        if (sessionToken && accessToken) {
            props.setAuthentication({
                sessionToken: sessionToken,
                accessToken: accessToken,
            })
        }
        navigate('/')
    })

    return <h1>Logging you in.</h1>
}

export default NewSession;