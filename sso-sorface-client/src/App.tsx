import React from 'react';
import { HashRouter } from 'react-router-dom';
import { AppRoutes } from './routes/AppRoutes';

import './App.css';
import { REACT_APP_VERSION } from './config';

function App() {
  return (
    <HashRouter>
      <div className="App">
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
