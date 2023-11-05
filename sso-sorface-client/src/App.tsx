import React from 'react';
import { HashRouter } from 'react-router-dom';
import { AppRoutes } from './routes/AppRoutes';
import { Link } from 'react-router-dom';
import { Captions, pathnames } from './constants';

import './App.css';
import { REACT_APP_VERSION } from './config';

function App() {
  return (
    <HashRouter>
      <div className="App">
        <header className="App-header">
          <h1><Link to={pathnames.home}>{Captions.AppTitle}</Link></h1>
        </header>
        <div className="App-content">
          <AppRoutes />
        </div>
        <footer>
          <div>{REACT_APP_VERSION}</div>
        </footer>
      </div>
    </HashRouter>
  );
}

export default App;
