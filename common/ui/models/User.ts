export interface INewUser {
    username: string;
    email: string;
}
export class User {
    lastSeen!: Date;
    username!: string;
    email!: string;
    password!: string;
}
