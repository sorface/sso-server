import React from 'react';
import {BrowserRouter} from 'react-router-dom';
import {AppRoutes} from './routes/AppRoutes';

import './App.css';
import {REACT_APP_VERSION} from './config';
import {Footer} from "./components/Footer/Footer";

function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <div className="App-content">
                    <AppRoutes/>
                </div>
                <Footer version={REACT_APP_VERSION}/>
            </div>
        </BrowserRouter>
    );
}

export default App;
