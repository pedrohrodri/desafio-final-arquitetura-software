db = db.getSiblingDB('desafio-db');

if (db.projects.countDocuments() === 0) {
    print('Populando coleção projects...');
    db.projects.insertOne({
        name: "Projeto Demo",
        description: "Projeto de exemplo para demonstração",
        startDate: new Date(),
        estimatedEndDate: new Date(Date.now() + 1000 * 60 * 60 * 24 * 30),
        tasks: [
            {
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