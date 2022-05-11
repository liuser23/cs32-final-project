import React, {useState} from 'react';
import './App.css';
import {FormControlLabel, FormGroup, Slider, Switch, ToggleButton, ToggleButtonGroup} from "@mui/material";
import FormatAlignLeftIcon from '@mui/icons-material/FormatAlignLeft';
import FormatAlignCenterIcon from '@mui/icons-material/FormatAlignCenter';
import FormatAlignRightIcon from '@mui/icons-material/FormatAlignRight';
import FormatAlignJustifyIcon from '@mui/icons-material/FormatAlignJustify';
import Box from "@mui/material/Box";


function RecommenderInput(props : {title : string, type: number}) {

    const [alignment, setAlignment] = React.useState<string | null>('left');

    const handleAlignment = (
        event: React.MouseEvent<HTMLElement>,
        newAlignment: string | null,
    ) => {
        setAlignment(newAlignment);
    };

    let inputSwitch = <></>;

    if (props.type === 0) {
        inputSwitch =
            <div className={"Recommender-switch"}>
                <ToggleButtonGroup
                    value={alignment}
                    exclusive
                    onChange={handleAlignment}
                    aria-label="recommendation type"
                >
                    <ToggleButton value="left" aria-label="left aligned">
                        Similar
                    </ToggleButton>
                    <ToggleButton value="center" aria-label="centered">
                        Different
                    </ToggleButton>
                    </ToggleButtonGroup>
            </div>
    } else if (props.type === 1) {
        inputSwitch =
            <div className={"Recommender-switch"}>
                <Box width={300}>
                <Slider defaultValue={50} aria-label="Default" valueLabelDisplay="auto" step={10}
                        marks
                        min={10}
                        max={100} color={"secondary"}/>
                </Box>
            </div>
    }

    return (
        <div className={"Recommender-input"}>
            <div className={"Recommender-input-title"}>{props.title}</div>
            {inputSwitch}
        </div>
    )
}

export default RecommenderInput;