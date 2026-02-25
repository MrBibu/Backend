import requests

# 1. Login
login_data = {
    "username": "student_hari",
    "password": "password"
}
response = requests.post("http://192.168.1.67:8081/api/auth/login", json=login_data)
print(f"Login Response: {response.status_code}")
print(response.text)

if response.status_code == 200:
    token = response.json().get("token")
    print(f"Token: {token}")

    # 2. Access course materials
    headers = {
        "Authorization": f"Bearer {token}"
    }
    materials_response = requests.get("http://192.168.1.67:8081/api/course-materials", headers=headers)
    print(f"Materials Response: {materials_response.status_code}")
    print(materials_response.text)
