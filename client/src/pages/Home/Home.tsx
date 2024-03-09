import {FunctionComponent, useEffect} from 'react';
import {useApiMethod} from '../../hooks/useApiMethod';
import {accountsApiDeclaration} from '../../apiDeclarations';

export interface Profile {

    id: string;

    email: string;

    firstName: string;

    lastName: string;

}

export const Home: FunctionComponent = () => {
    const {apiMethodState, fetchData} = useApiMethod<Profile, undefined>(accountsApiDeclaration.current);

    const {
        process: {
            loading,
            error
        },
        data
    } = apiMethodState;

    useEffect(() => {
        fetchData(undefined)
            .then(it => {
                console.log(it)
            })
            .catch(it => {
                console.log(it)
            })
    }, []);

    useEffect(() => {
        if (!data) {
            return;
        }
    }, [data]);

    return (
        <div>
            {loading ? (<p>Loading</p>) : (<p>Success</p>)}
            <p>
                {JSON.stringify(data)}
            </p>
        </div>
    );
};
