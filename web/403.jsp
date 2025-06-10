<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>403 - Access Denied</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;700&display=swap" rel="stylesheet">
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background: linear-gradient(135deg, #ff5e62, #6b7280);
            font-family: 'Poppins', sans-serif;
            overflow: hidden;
            position: relative;
        }
        .error-container {
            text-align: center;
            max-width: 700px;
            padding: 40px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 20px;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
            backdrop-filter: blur(10px);
            -webkit-backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.2);
            animation: fadeIn 1.2s ease-in-out;
        }
        .error-code {
            font-size: 130px;
            font-weight: 900;
            color: #dc3545;
            text-shadow: 3px 3px 6px rgba(0, 0, 0, 0.2);
            animation: pulse 2.5s infinite;
        }
        .error-message {
            font-size: 30px;
            font-weight: 700;
            color: #ffffff;
            margin-bottom: 20px;
            text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.3);
        }
        .error-description {
            font-size: 16px;
            color: #e2e8f0;
            margin-bottom: 30px;
        }
        .btn-back {
            font-size: 18px;
            padding: 12px 35px;
            border-radius: 50px;
            background: linear-gradient(45deg, #007bff, #00d4ff);
            border: none;
            color: white;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .btn-back:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0, 123, 255, 0.4);
            background: linear-gradient(45deg, #0057b3, #00b7e6);
        }
        .icon-lock {
            font-size: 60px;
            color: #dc3545;
            margin-bottom: 20px;
            animation: sway 3s infinite ease-in-out;
        }
        /* Particles Background */
        #particles {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            z-index: -1;
        }
        @keyframes fadeIn {
            0% { opacity: 0; transform: translateY(30px); }
            100% { opacity: 1; transform: translateY(0); }
        }
        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.06); }
            100% { transform: scale(1); }
        }
        @keyframes sway {
            0% { transform: rotate(0deg); }
            50% { transform: rotate(10deg); }
            100% { transform: rotate(0deg); }
        }
    </style>
</head>
<body>
    <canvas id="particles"></canvas>
    <div class="error-container">
        <i class="fas fa-lock icon-lock"></i>
        <div class="error-code">403</div>
        <div class="error-message">Access Denied</div>
        <p class="error-description">Sorry, you do not have permission to access this page. Please check your access rights or contact the administrator for assistance.</p>
        <a href="/WarehouseManagement/login" class="btn btn-back">Back to Homepage</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    <script>
        // Particles Background
        const canvas = document.getElementById('particles');
        const ctx = canvas.getContext('2d');
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;

        const particlesArray = [];
        const numberOfParticles = 50;

        class Particle {
            constructor() {
                this.x = Math.random() * canvas.width;
                this.y = Math.random() * canvas.height;
                this.size = Math.random() * 5 + 1;
                this.speedX = Math.random() * 3 - 1.5;
                this.speedY = Math.random() * 3 - 1.5;
            }
            update() {
                this.x += this.speedX;
                this.y += this.speedY;
                if (this.size > 0.2) this.size -= 0.1;
            }
            draw() {
                ctx.fillStyle = 'rgba(255, 255, 255, 0.8)';
                ctx.beginPath();
                ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
                ctx.fill();
            }
        }

        function init() {
            for (let i = 0; i < numberOfParticles; i++) {
                particlesArray.push(new Particle());
            }
        }

        function animate() {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
            for (let i = 0; i < particlesArray.length; i++) {
                particlesArray[i].update();
                particlesArray[i].draw();
                if (particlesArray[i].size <= 0.2) {
                    particlesArray.splice(i, 1);
                    i--;
                    particlesArray.push(new Particle());
                }
            }
            requestAnimationFrame(animate);
        }

        init();
        animate();

        window.addEventListener('resize', () => {
            canvas.width = window.innerWidth;
            canvas.height = window.innerHeight;
        });
    </script>
</body>
</html>