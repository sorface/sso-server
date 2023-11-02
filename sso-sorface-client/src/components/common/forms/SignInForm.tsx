import React from "react";


export class SignInForm extends React.Component<any, any> {

    constructor(props: any) {
        super(props);
        // Don't call this.setState() here!
        this.state = {counter: 0};
    }

    requestUserRegistration = () => {
        fetch("/api/users/registry")
            .then(it => console.log(it))
            .catch(it => console.log(it));
    }

    render() {
        return <div>
            <p>
                <h1>SSO Sorface</h1>
            </p>

            <p>
                <h1>user registration</h1>
            </p>

            <p>Email</p>
            <input placeholder="value"/>
            <p>Login</p>
            <input placeholder="value"/>
            <p>First Name</p>
            <input placeholder="value"/>
            <p>
                <button onClick={this.requestUserRegistration}>Sign Up</button>
            </p>
        </div>
    }
}