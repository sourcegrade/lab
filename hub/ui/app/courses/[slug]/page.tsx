export default function Page({ params }: { params: { slug: string } }) {
    return (
        <div>
            <h1>Course {params.slug}</h1>
            <p>TODO</p>
        </div>
    );
}
