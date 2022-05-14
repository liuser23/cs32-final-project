import React from 'react';
//import '../App.css';
import './suggestions/App.css'
import {Container} from 'react-bootstrap'
import Button from '@mui/material/Button';
import AppGif from './suggestions/Hnet-image.gif'
import Typewriter from 'typewriter-effect'

function Unauthenticated() {
    console.log()
    // className="d-flex justify-content-center align-items-center"

    return (
        <>
            <div className="full-screen" style={{flex: 1, padding: '120px'}}>
                <Container className="log-in-screen">
                    <div className="welcome-line" id="welcomeLine">
                        <h1 style={{padding: "10px", fontSize: "50px"}}>Welcome to </h1>
                        <h4 className="neonText">Friendify</h4>
                    </div>

                    <h1 style={{padding: "10px", fontSize: "50px"}}>the best Spotify social media
                        platform</h1>
                    {/*<p style={{padding: "20px", fontSize: "25px"}}>Take your first step toward making new friends by signing in with your existing Spotify account</p>*/}
                    <div className={"typewriter-style "}>
                        <Typewriter
                            onInit={(typewriter) => {
                                typewriter.typeString('Take your first step toward making new friends by signing in with your existing Spotify account')
                                    .callFunction(() => {
                                        console.log('String typed out!');
                                    })
                                    .start()
                            }}
                        />
                    </div>
                    <div className="button-gif">
                        <div>
                            <img src={AppGif} alt="gif" style={{
                                height: '270px',
                                margin: '20px',
                                marginLeft: '40px',
                                border: '5px solid rgb(0, 0, 0)'
                            }}/>
                        </div>
                        <div className="button-log" style={{margin: '40px'}}>
                            <form style={{padding: "40px"}} action={process.env.REACT_APP_LOGIN_REDIRECT}>
                                <Button variant="outlined" color="success" size="large" className="login-button"
                                        type='submit' style={{
                                    width: "120px",
                                    height: "50px",
                                    fontSize: '20px',
                                    fontWeight: 'bold',
                                    border: '5x solid'
                                }}>Log in</Button>
                            </form>
                        </div>

                    </div>

                </Container>
            </div>
        </>
    )
}

export default Unauthenticated;