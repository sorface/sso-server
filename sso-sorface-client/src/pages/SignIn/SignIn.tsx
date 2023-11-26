import React, {FormEvent, FunctionComponent, useEffect} from 'react';
import {useApiMethod} from '../../hooks/useApiMethod';
import {signInApiDeclaration} from '../../apiDeclarations';
import {Captions, pathnames} from '../../constants';
import {Field, Form} from '../../components/Form/Form';
import './SignIn.css';
import yandexLogo from './img/yandex.svg';
import googleLogo from './img/google.svg';
import githubLogo from './img/github.svg';
import {Link} from "react-router-dom";

const fields: Field[] = [
    {
        name: 'username',
        placeholder: Captions.Email,
        autoComplete: 'username',
        required: true,
        error: 'User with email or username already exists',
    },
    {
        name: 'password',
        placeholder: Captions.Password,
        type: 'password',
        autoComplete: 'current-password',
        required: true,
        error: 'Password invalid',
    },
];

export const SignIn: FunctionComponent = () => {
    const {apiMethodState, fetchData} = useApiMethod<unknown, FormData>(signInApiDeclaration.login);
    const {process: {loading, error}, data} = apiMethodState;

    useEffect(() => {
        if (!data) {
            return;
        }
        alert(`Response data: ${JSON.stringify(data)}`)
    }, [data]);

    const handleSignUp = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const form = event.target as HTMLFormElement;
        const data = new FormData(form);
        fetchData(data);
    };

    return (
        <div>
            <div className="form-sign-content">
                <div className="container-sing-form">
                    <div className="btn-container">
                        <a className="enter-btn" href={"/oauth2/authorization/github"}><img src={githubLogo}
                                                                                            alt="sign in with github"/></a>
                        <a className="enter-btn" href={"/oauth2/authorization/yandex"}><img src={yandexLogo}
                                                                                            alt="sign in with yandex"/></a>
                        <a className="enter-btn" href={"/oauth2/authorization/google"}><img src={googleLogo}
                                                                                            alt="sign in with google"/></a>
                    </div>
                </div>
            </div>
{/*            <Form
                httpMethod="POST"
                url="/login"
                fields={fields}
                loading={loading}
                error={error}
                submitCaption={Captions.SignIn}
            >
                <Link to={pathnames.signUp}>{Captions.SignUpLink}</Link>
                <p>
                    <a href={"/oauth2/authorization/github"}>{Captions.SignInGitHub}</a>
                </p>
                <p>
                    <a href={"/oauth2/authorization/yandex"}>{Captions.SignInYandex}</a>
                </p>
                <p>
                    <a href={"/oauth2/authorization/google"}>{Captions.SignInGoogle}</a>
                </p>
            </Form>*/}
        </div>
    );
};
