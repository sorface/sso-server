import React, {Fragment, FunctionComponent, MouseEvent, useState} from 'react';
import {Captions, pathnames} from '../../constants';
import {Field} from '../../components/Form/Form';
import githubLogo from './img/github.svg';
import yandexLogo from './img/yandex.svg';
import googleLogo from './img/google.svg';
import twitchLogo from './img/twitch.svg';
import emailLogo from './img/email-svgrepo-com.svg';
import {FormWrapper} from '../../components/Form/FormWrapper';
import {Link} from 'react-router-dom';

import './SignIn.css';

const emailFields: Field[] = [
    {
        name: 'username',
        placeholder: Captions.Email,
        autoComplete: 'username',
        required: true,
    },
    {
        name: 'password',
        placeholder: Captions.Password,
        type: 'password',
        autoComplete: 'current-password',
        required: true,
    },
];

export const SignIn: FunctionComponent = () => {
    const [withEmail, setWithEmail] = useState(false);

    const signinItems = [
        {
            name: 'Google',
            href: '/oauth2/authorization/google',
            logo: googleLogo,
        },
        {
            name: 'Yandex',
            href: '/oauth2/authorization/yandex',
            logo: yandexLogo,
        },
        {
            name: 'Github',
            href: '/oauth2/authorization/github',
            logo: githubLogo,
        },
        {
            name: 'Twitch',
            href: '/oauth2/authorization/twitch',
            logo: twitchLogo,
        },
        {
            name: 'Email',
            disabled: false,
            href: '#',
            logo: emailLogo,
            fields: withEmail ? emailFields : null,
            onClick: (event: MouseEvent) => {
                event.preventDefault();
                setWithEmail(!withEmail);
            }
        },
    ];

    return (
        <div className='page-sign'>
            <FormWrapper>
                <div className="signin-container">
                    <h1>{Captions.WelcomeToSSO}</h1>
                    {signinItems.map(signinItem => (
                        <Fragment key={signinItem.name}>
                            {signinItem.fields ? (
                                <>
                                    <hr/>
                                    <form action="/api/accounts/signin" method="post">
                                        <div>
                                            <label htmlFor="name">Email or Username: </label>
                                            <input type="text" name="username" id="name" required/>
                                        </div>
                                        <div className="form-example">
                                            <label htmlFor="password">Password: </label>
                                            <input type="password" name="password" id="password" required/>
                                        </div>
                                        <div className="form-example">
                                            <input type="submit" value="Войти"/>
                                        </div>
                                    </form>
                                    <Link className="signUp-link" to={pathnames.signUp}>{Captions.SignUpLink}</Link>
                                </>
                            ) : (
                                <a
                                    className="signin-link"
                                    href={signinItem.href}
                                    onClick={signinItem.onClick}
                                >
                                    <button className='signin-button' disabled={signinItem.disabled}>
                                        <img
                                            className="signin-logo"
                                            src={signinItem.logo}
                                            alt={`${signinItem.name} ${Captions.LogoAlt}`}
                                        />
                                        <span className='signin-caption'>
                                            {Captions.ContinueWith} {signinItem.name}
                                        </span>
                                    </button>
                                </a>
                            )}
                        </Fragment>
                    ))}
                </div>
            </FormWrapper>
        </div>
    );
};
