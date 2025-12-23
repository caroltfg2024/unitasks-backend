# UniTasks API Documentation

## Descripción
UniTasks es una aplicación **To-Do (Gestor de Tareas)** que permite a los usuarios crear, gestionar y organizar sus tareas con diferentes estados y prioridades.

## Base URL
```
http://localhost:8080/api
```

---

## 👤 User Endpoints

### 1. Register User (Crear usuario)
**Endpoint:** `POST /users`

**Description:** Registra un nuevo usuario en la aplicación. Encripta la contraseña y valida que el email no esté duplicado.

**Request:**
```json
{
  "name": "miguel",
  "lastname": "hernandez",
  "email": "miguel@example.com",
  "password": "password123"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "miguel",
  "lastname": "hernandez",
  "email": "miguel@example.com",
  "password": "$2a$10$...", 
  "active": true
}
```

**Status Codes:**
- `201 Created` - Usuario creado exitosamente
- `400 Bad Request` - Email ya existe o datos inválidos

---

### 2. Get All Users (Listar usuarios)
**Endpoint:** `GET /users`

**Description:** Obtiene la lista completa de todos los usuarios registrados.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "miguel",
    "lastname": "hernandez",
    "email": "miguel@example.com",
    "password": "$2a$10$...",
    "active": true
  },
  {
    "id": 2,
    "name": "ana",
    "lastname": "garcia",
    "email": "ana@example.com",
    "password": "$2a$10$...",
    "active": true
  }
]
```

**Status Codes:**
- `200 OK` - Lista retornada exitosamente

---

### 3. Get User by ID (Obtener usuario por ID)
**Endpoint:** `GET /users/{id}`

**Description:** Obtiene los detalles de un usuario específico por su ID.

**Example:** `GET /users/1`

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "miguel",
  "lastname": "hernandez",
  "email": "miguel@example.com",
  "password": "$2a$10$...",
  "active": true
}
```

**Status Codes:**
- `200 OK` - Usuario encontrado
- `404 Not Found` - Usuario no existe

---

### 4. Get User by Email (Buscar por email)
**Endpoint:** `GET /users/email?email={email}`

**Description:** Busca un usuario por su dirección de email.

**Example:** `GET /users/email?email=miguel@example.com`

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "miguel",
  "lastname": "hernandez",
  "email": "miguel@example.com",
  "password": "$2a$10$...",
  "active": true
}
```

**Status Codes:**
- `200 OK` - Usuario encontrado
- `404 Not Found` - Usuario no existe

---

### 5. Update User (Actualizar usuario)
**Endpoint:** `PUT /users/{id}`

**Description:** Actualiza los datos básicos de un usuario (name, lastname, email). No actualiza la contraseña.

**Example:** `PUT /users/1`

**Request:**
```json
{
  "name": "Miguel Angel",
  "lastname": "Hernandez Yebra",
  "email": "miguel.updated@example.com"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Miguel Angel",
  "lastname": "Hernandez Yebra",
  "email": "miguel.updated@example.com",
  "password": "$2a$10$...",
  "active": true
}
```

**Status Codes:**
- `200 OK` - Usuario actualizado
- `404 Not Found` - Usuario no existe
- `400 Bad Request` - Email ya está en uso

---

### 6. Change Password (Cambiar contraseña)
**Endpoint:** `PATCH /users/{id}/password`

**Description:** Cambia la contraseña de un usuario. Requiere la contraseña actual para validación.

**Example:** `PATCH /users/1/password?oldPassword=password123&newPassword=newPass456`

**Parameters:**
- `oldPassword` (query param) - Contraseña actual
- `newPassword` (query param) - Nueva contraseña

**Response (204 No Content):**
```
(Sin contenido)
```

**Status Codes:**
- `204 No Content` - Contraseña actualizada
- `404 Not Found` - Usuario no existe
- `400 Bad Request` - Contraseña antigua incorrecta

---

### 7. Activate User (Activar usuario)
**Endpoint:** `PATCH /users/{id}/activate`

**Description:** Activa un usuario (marca como activo).

**Example:** `PATCH /users/1/activate`

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "miguel",
  "lastname": "hernandez",
  "email": "miguel@example.com",
  "password": "$2a$10$...",
  "active": true
}
```

**Status Codes:**
- `200 OK` - Usuario activado
- `404 Not Found` - Usuario no existe

---

### 8. Deactivate User (Desactivar usuario)
**Endpoint:** `PATCH /users/{id}/deactivate`

**Description:** Desactiva un usuario (marca como inactivo/bloqueado).

**Example:** `PATCH /users/1/deactivate`

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "miguel",
  "lastname": "hernandez",
  "email": "miguel@example.com",
  "password": "$2a$10$...",
  "active": false
}
```

**Status Codes:**
- `200 OK` - Usuario desactivado
- `404 Not Found` - Usuario no existe

---

### 9. Delete User (Eliminar usuario)
**Endpoint:** `DELETE /users/{id}`

**Description:** Elimina un usuario y todas sus tareas asociadas (eliminación en cascada).

**Example:** `DELETE /users/1`

**Response (204 No Content):**
```
(Sin contenido)
```

**Status Codes:**
- `204 No Content` - Usuario eliminado
- `404 Not Found` - Usuario no existe

---

### 10. Count Users (Contar usuarios)
**Endpoint:** `GET /users/count`

**Description:** Retorna el número total de usuarios registrados.

**Response (200 OK):**
```json
5
```

**Status Codes:**
- `200 OK` - Conteo exitoso

---

## 📋 Task Endpoints

### 1. Create Task (Crear tarea)
**Endpoint:** `POST /users/{userId}/tasks`

**Description:** Crea una nueva tarea para un usuario específico.

**Example:** `POST /users/1/tasks`

**Request:**
```json
{
  "title": "Completar documentación API",
  "description": "Escribir la documentación completa de todos los endpoints",
  "status": "PENDING",
  "priority": "HIGH",
  "dueDate": "2025-12-31T23:59:59"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Completar documentación API",
  "description": "Escribir la documentación completa de todos los endpoints",
  "status": "PENDING",
  "priority": "HIGH",
  "createdAt": "2025-12-19T10:30:00",
  "updatedAt": "2025-12-19T10:30:00",
  "dueDate": "2025-12-31T23:59:59",
  "user": {
    "id": 1,
    "name": "miguel",
    "lastname": "hernandez",
    "email": "miguel@example.com",
    "password": "$2a$10$...",
    "active": true
  }
}
```

**Status Codes:**
- `201 Created` - Tarea creada exitosamente
- `404 Not Found` - Usuario no existe
- `400 Bad Request` - Datos inválidos (título vacío)

**Notes:**
- Si no se especifica `status`, se asigna `PENDING` por defecto
- Si no se especifica `priority`, se asigna `MEDIUM` por defecto
- Estados válidos: `PENDING`, `IN_PROGRESS`, `DONE`
- Prioridades válidas: `LOW`, `MEDIUM`, `HIGH`

---

### 2. Get All Tasks (Listar todas las tareas)
**Endpoint:** `GET /tasks`

**Description:** Obtiene la lista completa de todas las tareas del sistema.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Completar documentación API",
    "description": "Escribir la documentación completa de todos los endpoints",
    "status": "PENDING",
    "priority": "HIGH",
    "createdAt": "2025-12-19T10:30:00",
    "updatedAt": "2025-12-19T10:30:00",
    "dueDate": "2025-12-31T23:59:59",
    "user": { ... }
  },
  {
    "id": 2,
    "title": "Revisar código",
    "description": "Code review del módulo de autenticación",
    "status": "IN_PROGRESS",
    "priority": "MEDIUM",
    "createdAt": "2025-12-19T11:00:00",
    "updatedAt": "2025-12-19T11:30:00",
    "dueDate": "2025-12-25T18:00:00",
    "user": { ... }
  }
]
```

**Status Codes:**
- `200 OK` - Lista retornada exitosamente

---

### 3. Get Task by ID (Obtener tarea por ID)
**Endpoint:** `GET /tasks/{id}`

**Description:** Obtiene los detalles de una tarea específica por su ID.

**Example:** `GET /tasks/1`

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Completar documentación API",
  "description": "Escribir la documentación completa de todos los endpoints",
  "status": "PENDING",
  "priority": "HIGH",
  "createdAt": "2025-12-19T10:30:00",
  "updatedAt": "2025-12-19T10:30:00",
  "dueDate": "2025-12-31T23:59:59",
  "user": {
    "id": 1,
    "name": "miguel",
    "lastname": "hernandez",
    "email": "miguel@example.com",
    "password": "$2a$10$...",
    "active": true
  }
}
```

**Status Codes:**
- `200 OK` - Tarea encontrada
- `404 Not Found` - Tarea no existe

---

### 4. Update Task (Actualizar tarea)
**Endpoint:** `PUT /tasks/{id}`

**Description:** Actualiza los datos de una tarea (title, description, status, priority, dueDate). No cambia el usuario dueño.

**Example:** `PUT /tasks/1`

**Request:**
```json
{
  "title": "Completar documentación API - ACTUALIZADO",
  "description": "Escribir la documentación completa con ejemplos",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2025-12-30T23:59:59"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Completar documentación API - ACTUALIZADO",
  "description": "Escribir la documentación completa con ejemplos",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "createdAt": "2025-12-19T10:30:00",
  "updatedAt": "2025-12-19T15:45:00",
  "dueDate": "2025-12-30T23:59:59",
  "user": { ... }
}
```

**Status Codes:**
- `200 OK` - Tarea actualizada
- `404 Not Found` - Tarea no existe

---

### 5. Change Task Status (Cambiar estado)
**Endpoint:** `PATCH /tasks/{id}/status?status={status}`

**Description:** Cambia únicamente el estado de una tarea.

**Example:** `PATCH /tasks/1/status?status=DONE`

**Parameters:**
- `status` (query param) - Nuevo estado: `PENDING`, `IN_PROGRESS`, o `DONE`

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Completar documentación API",
  "description": "Escribir la documentación completa de todos los endpoints",
  "status": "DONE",
  "priority": "HIGH",
  "createdAt": "2025-12-19T10:30:00",
  "updatedAt": "2025-12-19T16:00:00",
  "dueDate": "2025-12-31T23:59:59",
  "user": { ... }
}
```

**Status Codes:**
- `200 OK` - Estado actualizado
- `404 Not Found` - Tarea no existe
- `400 Bad Request` - Estado inválido

---

### 6. Delete Task (Eliminar tarea)
**Endpoint:** `DELETE /tasks/{id}`

**Description:** Elimina una tarea del sistema.

**Example:** `DELETE /tasks/1`

**Response (204 No Content):**
```
(Sin contenido)
```

**Status Codes:**
- `204 No Content` - Tarea eliminada
- `404 Not Found` - Tarea no existe

---

### 7. Count All Tasks (Contar todas las tareas)
**Endpoint:** `GET /tasks/count`

**Description:** Retorna el número total de tareas en el sistema.

**Response (200 OK):**
```json
15
```

**Status Codes:**
- `200 OK` - Conteo exitoso

---

## 📊 Enums Reference

### TaskStatus (Estados de Tarea)
- `PENDING` - Tarea pendiente (por defecto)
- `IN_PROGRESS` - Tarea en progreso
- `DONE` - Tarea completada

### TaskPriority (Prioridades de Tarea)
- `LOW` - Prioridad baja
- `MEDIUM` - Prioridad media (por defecto)
- `HIGH` - Prioridad alta

---

## 🚨 Error Handling

Todas las excepciones personalizadas lanzan respuestas HTTP apropiadas:

### Common Error Responses

**404 Not Found - UserNotFoundException:**
```json
{
  "message": "Usuario no encontrado con ID: 1"
}
```

**404 Not Found - TaskNotFoundException:**
```json
{
  "message": "Tarea no encontrada con ID: 1"
}
```

**400 Bad Request - EmailAlreadyExistsException:**
```json
{
  "message": "Ya existe un usuario con el email: miguel@example.com"
}
```

**400 Bad Request - InvalidPasswordException:**
```json
{
  "message": "La contraseña actual no es correcta"
}
```

**400 Bad Request - InvalidTaskDataException:**
```json
{
  "message": "El título de la tarea es obligatorio"
}
```

**400 Bad Request - InvalidUserDataException:**
```json
{
  "message": "Datos de usuario inválidos"
}
```

---

## 🔒 Security Notes

- Las contraseñas se almacenan encriptadas usando **BCrypt**
- Los emails se normalizan (trim + toLowerCase) para garantizar unicidad
- Spring Security está temporalmente deshabilitado para desarrollo
- En producción, implementar autenticación JWT/OAuth2

---

## 🧪 Testing Examples

### Crear usuario y tarea con curl:

```bash
# 1. Crear usuario
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "miguel",
    "lastname": "hernandez",
    "email": "miguel@example.com",
    "password": "password123"
  }'

# 2. Crear tarea para el usuario (ID=1)
curl -X POST http://localhost:8080/api/users/1/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mi primera tarea",
    "description": "Descripción de la tarea",
    "priority": "HIGH"
  }'

# 3. Cambiar estado de la tarea a IN_PROGRESS
curl -X PATCH "http://localhost:8080/api/tasks/1/status?status=IN_PROGRESS"

# 4. Marcar tarea como DONE
curl -X PATCH "http://localhost:8080/api/tasks/1/status?status=DONE"
```

---

## 📝 Notes

- Todos los endpoints retornan JSON
- Las fechas usan formato ISO 8601: `2025-12-31T23:59:59`
- Los IDs son autogenerados por la base de datos
- La base de datos H2 en memoria se reinicia con cada arranque de la aplicación
- Console H2: http://localhost:8080/h2-console (jdbc:h2:mem:unitasks)
- La serialización JSON ignora proxys de Hibernate y evita recursión User <-> Task
