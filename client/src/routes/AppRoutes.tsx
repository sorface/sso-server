import { FunctionComponent, useContext } from 'react';
import { Route, Routes } from 'react-router-dom';
import { pathnames } from '../constants';
import { Account } from '../pages/Account/Account';
import { NotFound } from '../pages/NotFound/NotFound';
import { SignIn } from '../pages/SignIn/SignIn';
import { Activate } from '../pages/Activate/Activate';
import { Session } from "../pages/Session/Session";
import { ProtectedRoute } from './ProtectedRoute';
import { AuthContext } from '../context/AuthContext';

export const AppRoutes: FunctionComponent = () => {
    const account = useContext(AuthContext);
    const authenticated = !!account;
    const authenticatedRouteProps = {
        allowed: authenticated,
        redirect: pathnames.signIn,
    };
    const notAuthenticatedRouteProps = {
        allowed: !authenticated,
        redirect: pathnames.account,
    };

    return (
        <Routes>
            <Route path={pathnames.account}
                element={
                    <ProtectedRoute {...authenticatedRouteProps}>
                        <Account />
                    </ProtectedRoute>
                }
            />
            <Route path={pathnames.session}
                element={
                    <ProtectedRoute {...authenticatedRouteProps}>
                        <Session />
                    </ProtectedRoute>
                }
            />
            {/*<Route path={pathnames.signUp} element={<SignUp/>}/>*/}
            <Route path={pathnames.activate}
                element={
                    <ProtectedRoute {...authenticatedRouteProps}>
                        <Activate />
                    </ProtectedRoute>
                }
            />
            <Route path={pathnames.signIn}
                element={
                    <ProtectedRoute {...notAuthenticatedRouteProps}>
                        <SignIn />
                    </ProtectedRoute>
                }
            />
            <Route path="*" element={<NotFound />} />
        </Routes>
    );
};
