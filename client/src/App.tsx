import React, {useEffect} from 'react';
import {BrowserRouter} from 'react-router-dom';
import {AppRoutes} from './routes/AppRoutes';
import {REACT_APP_VERSION} from './config';
import {Menu} from './components/Menu/Menu';
import {Footer} from "./components/Footer/Footer";
import {Loader} from './components/Loader/Loader';
import {AuthContext} from './context/AuthContext';
import {useCsrfApi, useGetAccountApi} from './hooks/useGetAccount';
import {Captions} from './constants';

import './App.css';

function App() {

    const {csrfState, loadCsrf} = useCsrfApi();

    const {accountState, loadAccount} = useGetAccountApi();
    const {
        process: {
            loading,
            error,
        },
        account,
    } = accountState;

    const accountWillLoad = !account && !error;

    useEffect(() => {
        loadCsrf();
    }, [loadCsrf]);

    useEffect(() => {
        if (!csrfState.account) {
            return;
        }
        loadAccount();
    }, [csrfState.account, loadAccount]);

    if (loading || accountWillLoad) {
        return (
            <div className="App-container" data-testid="App-container">
                <div className="App">
                    <div className="App-content">
                        <p>{Captions.Loading}...</p>
                        <Loader/>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <BrowserRouter>
            <AuthContext.Provider value={account}>
                <div className="App-container" data-testid="App-container">
                    <Menu />
                    <div className="App">
                        <div className="App-content">
                            <AppRoutes />
                        </div>
                        <Footer version={REACT_APP_VERSION} />
                    </div>
                </div>
            </AuthContext.Provider>
        </BrowserRouter>
    );
}

export default App;
