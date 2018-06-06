
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<title> Login </title>
		<link type="text/css" href="css/bootstrap.css" rel="stylesheet" />	
		<script type="text/javascript" src="<c:url value="/js/jquery-1.8.3.js"/>"></script>
	</head>

	<style type="text/css">
	
      body {
        
        background-image: url("css/images/login.jpg"); 
 		background-repeat: no-repeat;
		background-size: 100%;
		background-position-x: 50%;

      }
      
      .alert-error {
		margin: auto;
		margin-top: 10px;
		width: 300px;
	  }
      
      .btn {
      	margin-right: 25px;
		margin-left: 20px;
		margin-top: -16px;
		padding: 5px 15px;
		font-size: 12px;
      }
      
      .inputLoginSenha{
      	width: 100px;
      	border-color: rgb(17, 109, 162);
      	text-align: center;
      }
      
      .labelLoginSenha{
      	
      	display: inline;
      	color: rgb(17, 109, 162);
      }

      .form-signin {
      	
      	right: 10px;
      	top: 10px;
		position: fixed;
      	
        max-width: 700px;
        padding: 21px 0px 5px 17px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }
      
      .esqueciminhasenha{
      	float: right;
		padding-right: 10px;
      }

    </style>
	
	<body>
	
		<c:if test="${not empty errors}">
			<div class="alert alert-error">
				<c:forEach items="${errors }" var="error">
					<strong>${error.category }</strong> - ${error.message } <br>
				</c:forEach>
			</div>
		</c:if>
	
		<div class="container">
	
	      <form class="form-signin" action="<c:url value="/login/efetuarLogin"/>" method="post" >
	      
		      <label class="labelLoginSenha" >Código:</label>  
		      <input type="text" class="inputLoginSenha numero-inteiro" name="usuario.id_Codigo" maxlength="8" >
		      <label class="labelLoginSenha" >Senha:</label>  
		      <input type="password" class="inputLoginSenha" name="usuario.informacoesFixasUsuario.senha" >
	        
	      	  <button class="btn btn-large btn-primary" type="submit" onclick="this.disabled=true;this.form.submit();" >Entrar</button>
	      	  
	      	  <br>
	      	  
	      	  <a class="esqueciminhasenha" href="<c:url value="/login/esqueciMinhaSenha"/>" > Esqueci minha senha </a>
	      	  
	      </form>
	      
	    </div>
	    
	    <script>
	    
		    jQuery(document).ready(function() {
	
		    	jQuery(".numero-inteiro").keypress(function(e) {
	
		    		var tecla = (window.event) ? event.keyCode : e.which;
	
		    		if ((tecla > 47 && tecla < 58))
		    			return true;
		    		
		    		else {
		    			
		    			if (tecla == 8 || tecla == 0)
		    				return true;
	
		    			else
		    				return false;
		    		}
		    	});
		    });
	    
	    </script>
		
	</body>
</html>