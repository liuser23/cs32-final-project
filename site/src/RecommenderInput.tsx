import React, {Dispatch, SetStateAction, useState} from 'react';
import './App.css';
import {FormControlLabel, FormGroup, Slider, Switch, ToggleButton, ToggleButtonGroup} from "@mui/material";
import FormatAlignLeftIcon from '@mui/icons-material/FormatAlignLeft';
import FormatAlignCenterIcon from '@mui/icons-material/FormatAlignCenter';
import FormatAlignRightIcon from '@mui/icons-material/FormatAlignRight';
import FormatAlignJustifyIcon from '@mui/icons-material/FormatAlignJustify';
import Box from "@mui/material/Box";


function RecommenderInput(props : {title : string, type: number, val : number | number[], updateValue : Dispatch<SetStateAction<number | number[]>>}) {

    const handleValue = (
        event: React.MouseEvent<HTMLElement>,
        newVal: number,
    ) => {
        props.updateValue(newVal);
    };

    let inputSwitch = <></>;

    if (props.type === 0) {
        inputSwitch =
            <div className={"Recommender-switch"}>
                <ToggleButtonGroup
                    value={props.val}
                    exclusive
                    onChange={handleValue}
                    aria-label="recommendation type"
                >
                    <ToggleButton value={1} aria-label="similar rec type">
                        Similar
                    </ToggleButton>
                    <ToggleButton value={0} aria-label="different rec type">
                        Different
                    </ToggleButton>
                    </ToggleButtonGroup>
            </div>
    } else if (props.type === 1) {
        inputSwitch =
            <div className={"Recommender-switch"}>
                <Box width={300}>
                <Slider defaultValue={50} onChange={(_, value) => props.updateValue(value)} aria-label="Default" valueLabelDisplay="auto" step={10}
                        marks
                        min={10}
                        max={100} color={"secondary"}/>
                </Box>
            </div>
    }

    return (
        <div id="recInput" className={"Recommender-input"}>
            <div id="recInputTitle" className={"Recommender-input-title"}>{props.title}</div>
            {inputSwitch}
        </div>
    )
}

export default RecommenderInput;