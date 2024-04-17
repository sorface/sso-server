import {useCallback, useReducer} from 'react';
import {REACT_APP_BACKEND_URL} from '../config';
import {Account} from '../types/account';
import {isPresentCsrfCookie, setupCsrf, X_CSRF_TOKEN_COOKIE_NAME} from "../utils/csrf";

interface GetMeState {
    process: {
        loading: boolean;
        error: string | null;
    };
    account: Account | null;
}

const initialState: GetMeState = {
    process: {
        loading: false,
        error: null,
    },
    account: null,
};

type GetMeAction = {
    name: 'startLoad';
} | {
    name: 'setAccount';
    payload: Account;
} | {
    name: 'setError';
    payload: string;
};

const getMeReducer = (state: GetMeState, action: GetMeAction): GetMeState => {
    switch (action.name) {
        case 'startLoad':
            return {
                process: {
                    loading: true,
                    error: null,
                },
                account: null,
            };
        case 'setError':
            return {
                ...state,
                process: {
                    loading: false,
                    error: action.payload
                }
            };
        case 'setAccount':
            return {
                process: {
                    loading: false,
                    error: null,
                },
                account: action.payload
            };
        default:
            return state;
    }
};

export const useCsrfApi = () => {
    const [csrfState, dispatch] = useReducer(getMeReducer, initialState);

    const loadCsrf = useCallback(async () => {
        dispatch({name: 'startLoad'});

        if (isPresentCsrfCookie()) {
            dispatch({
                name: 'setAccount', payload: {
                    id: '',
                    email: '',
                    firstName: '',
                    lastName: '',
                    avatar: ''
                }
            });
            return;
        }

        try {
            const response = await fetch(`${REACT_APP_BACKEND_URL}/csrf`, {
                credentials: 'include',
                mode: "cors"
            });
            if (!response.ok) {
                throw new Error('UserApi error');
            }
            const responseJson = await response.json();
            dispatch({name: 'setAccount', payload: responseJson});
        } catch (err: any) {
            dispatch({
                name: 'setError',
                payload: err.message || 'Failed to get me',
            });
        }
    }, []);

    return {
        csrfState,
        loadCsrf,
    };
};


export const useGetAccountApi = () => {
    const [accountState, dispatch] = useReducer(getMeReducer, initialState);

    const loadAccount = useCallback(async () => {
        dispatch({name: 'startLoad'});

        const headers = new Headers();

        setupCsrf(X_CSRF_TOKEN_COOKIE_NAME, headers);

        try {
            const response = await fetch(`${REACT_APP_BACKEND_URL}/api/accounts/current`, {
                headers,
                mode: "cors"
            });
            if (!response.ok) {
                throw new Error('UserApi error');
            }
            const responseJson = await response.json();
            dispatch({name: 'setAccount', payload: responseJson});
        } catch (err: any) {
            dispatch({
                name: 'setError',
                payload: err.message || 'Failed to get me',
            });
        }
    }, []);

    return {
        accountState,
        loadAccount,
    };
};
