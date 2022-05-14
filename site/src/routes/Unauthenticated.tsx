import React from 'react';
//import '../App.css';
import './suggestions/App.css'
import { Container } from 'react-bootstrap'
import Button from '@mui/material/Button';
import AppGif from './suggestions/Hnet-image.gif'

function Unauthenticated() {
    console.log()
    // className="d-flex justify-content-center align-items-center"

    return (
        <>
            <div className="full-screen" style={{flex: 1, padding: '120px'}}>
            <Container className="log-in-screen">
                <h1 style={{padding: "20px", fontSize: "50px"}}>Welcome to the best Spotify social media platform</h1>
                <p style={{padding: "20px", fontSize: "25px"}}>Take your first step toward making new friends by signing in with your existing Spotify account</p>

                <div className="button-gif">
                    <div className="button-log" style={{margin: '40px'}}>
                        <form style={{padding: "20px"}} action={process.env.REACT_APP_LOGIN_REDIRECT}>
                            <Button variant="outlined" color="success" size="large" className="login-button" type='submit' >Log in</Button>
                        </form>
                    </div>
                   <div>
                    <img src={AppGif} alt="gif" style={{height: '270px', margin: '20px', border: '5px solid rgb(0, 0, 0)'}}/>
                   </div>
                </div>

            </Container>
            </div>
        </>
    )
}

export default Unauthenticated;