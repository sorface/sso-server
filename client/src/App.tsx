import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { AppRoutes } from './routes/AppRoutes';
import { REACT_APP_VERSION } from './config';
import { Menu } from './components/Menu/Menu';

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
                    <footer>
                        <div>{REACT_APP_VERSION}</div>
                    </footer>
                </div>
            </div>
        </BrowserRouter>
    );
}

export default App;
