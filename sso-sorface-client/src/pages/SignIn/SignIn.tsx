import React, { FunctionComponent, useState, MouseEvent, Fragment } from 'react';
import { Captions, pathnames } from '../../constants';
import { Field } from '../../components/Form/Form';
import { FormFields } from '../../components/Form/FormFields';
import githubLogo from './img/github.svg';
import yandexLogo from './img/yandex.svg';
import googleLogo from './img/google.svg';
import emailLogo from './img/email-svgrepo-com.svg';
import './SignIn.css';
import { FormWrapper } from '../../components/Form/FormWrapper';
import { Link } from 'react-router-dom';

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
            name: 'Email',
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
                                    <hr />
                                    <form method='POST' action='/api/login'>
                                        <FormFields fields={signinItem.fields} />
                                        <input type="submit" value={Captions.SignIn} />
                                    </form>
                                    <Link className="signUp-link" to={pathnames.signUp}>{Captions.SignUpLink}</Link>
                                </>
                            ) : (
                                <a
                                    className="signin-link"
                                    href={signinItem.href}
                                    onClick={signinItem.onClick}
                                >
                                    <button className='signin-button'>
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
