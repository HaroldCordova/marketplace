# Marketplace Web para Emprendimientos Locales por Categorías

Proyecto integrador (Angular + Spring Boot) desarrollado en equipo.

## Monorepo
```
/frontend   # Angular
/backend    # Spring Boot
/docs       # Diagramas, wireframes, DER, etc.
```
## Flujo de trabajo (Git)
- Rama `main`: código estable y listo para release.
- Rama `develop`: integración de features.
- Ramas de tarea: `feature/<nombre>`, `fix/<nombre>`, `chore/<nombre>`.

### Pasos básicos
```bash
git clone <URL_DEL_REPO>
git checkout -b feature/<tarea>
# ... cambios ...
git add .
git commit -m "feat(productos): crear listado inicial"
git push origin feature/<tarea>
# Abrir Pull Request → revisión → merge a develop → merge a main en releases
```

## Backlog inicial
- Autenticación JWT (roles: cliente, vendedor, admin).
- CRUD productos + categorías.
- Carrito y pedidos.
- Panel vendedor y admin.
- Documentación de API (Swagger).

## Scripts útiles (cuando existan los proyectos)
- Frontend: `npm run start`, `npm run build`
- Backend: `./mvnw spring-boot:run` o `mvn spring-boot:run`