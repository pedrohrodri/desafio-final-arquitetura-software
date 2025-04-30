db = db.getSiblingDB('desafio-db');

if (db.projects.countDocuments() === 0) {
    print('Populando coleção projects...');
    db.projects.insertOne({
        _id: "3ad47cd5-4243-4066-b131-4447ca64c17e",
        name: "Projeto Demo",
        description: "Projeto de exemplo para demonstração",
        startDate: new Date(),
        estimatedEndDate: new Date(Date.now() + 1000 * 60 * 60 * 24 * 30),
        tasks: [
            {
                _id: "be7ba221-b15f-4ab6-a2bf-5c9a39205eb5",
                title: "Tarefa Demo",
                status: "OPEN",
                dueDate: new Date(Date.now() + 1000 * 60 * 60 * 24 * 7),
                priority: "MEDIUM",
                assignee: null,
                createdAt: new Date(),
                updatedAt: new Date()
            }
        ],
        createdAt: new Date(),
        updatedAt: new Date()
    });
} else {
    print('Coleção projects já populada, pulando.');
}