import {CsrfToken, isPresentCsrfCookie} from "../utils/csrf";
import {useCallback, useReducer} from "react";
import {REACT_APP_BACKEND_URL} from "../config";

interface CsrfConfigState {
    process: {
        loading: boolean;
        error: string | null;
    };
    csrfConfig: CsrfToken | null;
}

const initialState: CsrfConfigState = {
    process: {
        loading: false,
        error: null,
    },
    csrfConfig: null,
};

type GetCsrfAction = {
    name: 'startLoad';
} | {
    name: 'setCsrfConfig';
    payload: CsrfToken;
} | {
    name: 'setError';
    payload: string;
};

const getCsrfConfigReducer = (state: CsrfConfigState, action: GetCsrfAction): CsrfConfigState => {
    switch (action.name) {
        case 'startLoad':
            return {
                process: {
                    loading: true,
                    error: null,
                },
                csrfConfig: null,
            };
        case 'setError':
            return {
                ...state,
                process: {
                    loading: false,
                    error: action.payload
                }
            };
        case 'setCsrfConfig':
            return {
                process: {
                    loading: false,
                    error: null,
                },
                csrfConfig: action.payload
            };

        default:
            return state;
    }
};

export const useCsrfApi = () => {
    const [csrfConfigState, dispatch] = useReducer(getCsrfConfigReducer, initialState);

    const loadCsrfConfig = useCallback(async () => {
        dispatch({name: 'startLoad'});

        if (isPresentCsrfCookie()) {
            dispatch({
                name: 'setCsrfConfig',
                payload: {
                    parameterName: '',
                    token: '',
                    headerName: ''
                } as CsrfToken
            });
            return;
        }

        try {
            const response = await fetch(`${REACT_APP_BACKEND_URL}/api/csrf`);
            if (!response.ok) {
                throw new Error('UserApi error');
            }
            const responseJson = await response.json();
            dispatch({name: 'setCsrfConfig', payload: responseJson});
        } catch (err: any) {
            dispatch({
                name: 'setError',
                payload: err.message || 'Failed to get csrf config',
            });
        }
    }, []);

    return {
        csrfConfigState,
        loadCsrfConfig,
    };
};
