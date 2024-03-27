import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { AppRoutes } from './routes/AppRoutes';
import { REACT_APP_VERSION } from './config';
import { Menu } from './components/Menu/Menu';
import {Footer} from "./components/Footer/Footer";

import './App.css';

function App() {
    return (
        <BrowserRouter>
            <div className="App-container">
                <Menu />
                <div className="App">
                    <div className="App-content">
                        <AppRoutes />
                    </div>
                    <Footer version={REACT_APP_VERSION}/>
                </div>
            </div>
        </BrowserRouter>
    );
}

export default App;
