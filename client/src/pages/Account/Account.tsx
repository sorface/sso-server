import {FunctionComponent, useEffect} from 'react';
import {useApiMethod} from '../../hooks/useApiMethod';
import {accountsApiDeclaration} from '../../apiDeclarations';
import defaultAvatarImage from './img/anonymus_avatar.png';

import './Account.css';
import {Link} from "react-router-dom";
import {Captions, pathnames} from "../../constants";

export interface Profile {

    id: string;

    email: string;

    firstName: string;

    lastName: string;

    avatar: string;

}

export const Account: FunctionComponent = () => {
    const {apiMethodState, fetchData} = useApiMethod<Profile, undefined>(accountsApiDeclaration.current);
    const {
        apiMethodState: logoutMethodState,
        fetchData: logoutFetch
    } = useApiMethod<Profile, undefined>(accountsApiDeclaration.logout);

    const {
        process: {
            loading,
            error
        },
        data
    } = apiMethodState;

    const {
        process: {
            loading: logoutLoading,
            error: logoutError
        },
        data: logoutData
    } = logoutMethodState;

    useEffect(() => {
        fetchData(undefined);
    }, []);

    useEffect(() => {
        if (!data) {
            return;
        }
    }, [data]);

    useEffect(() => {
        if (!logoutData) {
            return;
        }

        window.location.assign(pathnames.signIn)
    }, [logoutData])

    const handleLogout = () => {
        logoutFetch(undefined)
    }

    return (
        <div>
            {loading ? (<p>Loading</p>) : (<>
                <div>
                    <div className="avatar">
                        <img src={data?.avatar ? data?.avatar : defaultAvatarImage} alt="avatar"/>
                    </div>
                    <table className="user-data-table">
                        <tr>
                            <td className="bold left">id</td>
                            <td className="right">{data?.id}</td>
                        </tr>
                        <tr>
                            <td className="bold left">email</td>
                            <td className="right">{data?.email}</td>
                        </tr>
                        <tr>
                            <td className="bold left">first name</td>
                            <td className="right">{data?.firstName ? data?.firstName : 'unknown'}</td>
                        </tr>
                        <tr>
                            <td className="bold left">last name</td>
                            <td className="right">{data?.lastName ? data?.lastName : 'unknown'}</td>
                        </tr>
                    </table>
                </div>

                <p>
                    <Link to={pathnames.session}>{Captions.ActiveSessions}</Link>
                </p>

                <button className="exit-button" onClick={handleLogout}>Logout</button>
            </>)}
        </div>
    );
};
