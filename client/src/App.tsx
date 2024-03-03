import React from 'react';
import {BrowserRouter} from 'react-router-dom';
import {AppRoutes} from './routes/AppRoutes';

import './App.css';
import {REACT_APP_VERSION} from './config';

function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <div className="App-content">
                    <AppRoutes/>
                </div>
                <footer>
                    <div>current version: {REACT_APP_VERSION}</div>
                </footer>
            </div>
        </BrowserRouter>
    );
}

export default App;
