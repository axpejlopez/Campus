# 🎓 CampusManager

Proyecto de ejemplo para aprender desarrollo colaborativo con **Spring Boot**, **Git** y **GitHub**.

El objetivo es crear una API REST que permita gestionar **alumnos**, **profesores**, **cursos** y **matrículas** dentro de un campus educativo.

Cada participante desarrollará su propio módulo de forma independiente, siguiendo las tareas asignadas y trabajando en ramas separadas.

---

## 🚀 Objetivos del proyecto

- Practicar el flujo completo de trabajo en **Git** (branch → commit → push → pull request).
- Construir una API REST funcional con **Spring Boot**.
- Aprender la estructura por capas: `domain`, `repository`, `service`, `controller`.
- Usar **H2** como base de datos embebida con datos de prueba (`data.sql`).
- Entender cómo se integran las diferentes entidades del modelo.

---

## 🧱 Estructura del proyecto
src/main/java/com/example/campusmanager/

┣ controller/ → controladores REST (endpoints)

┣ domain/ → entidades JPA (modelo de datos)

┣ repository/ → interfaces que extienden JpaRepository

┗ service/ → lógica de negocio
