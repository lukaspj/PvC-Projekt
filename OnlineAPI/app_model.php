﻿<?php
class App_model extends CI_Model {
	public function usernameavailable($username)
	{
		$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM app_users WHERE username='$username'");
		if($query->num_rows() > 1)
			return -1;
		else if($query->num_rows() == 1)
			return false;
		else
			return true;
	}
	
	public function verify_user($username, $pass)
	{
		$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM app_users WHERE username='$username' AND password='$pass'");
		if($query->num_rows() > 1)
			return -1;
		else if($query->num_rows() == 1)
			return false;
		else
			return true;
	}
	
	public function allAppUsers()
	{
		$CI =& get_instance();
		return $CI->db->query("SELECT username, X(position) AS x, Y(position) AS y, bluetoothid FROM app_users");
	}
	
	//http://howto-use-mysql-spatial-ext.blogspot.dk/
	public function updatePosition($username, $lat, $long)
	{
		$CI =& get_instance();
		$query = $CI->db->query("UPDATE app_users SET position=GeomFromText('POINT($lat $long)') WHERE username='$username'");
		return $CI->db->_error_number();
	}
	
	public function registerUser($username, $lat, $long, $bluetoothID, $password)
	{
		$CI =& get_instance();
		$query = $CI->db->query("INSERT INTO app_users (username, position, bluetoothid, password) VALUES ('$username', GeomFromText('POINT($lat $long)'), $bluetoothID, $password)");
		return $CI->db->_error_number();
	}

	public function addUser($username, $password, $email)
    {
    	$CI =& get_instance();
		$data = array('username' => "".$username,
						'password' => "".$password,
						'email' => "".$email);
        $CI->db->insert('nn_users', $data);
		return $CI->db->_error_number();
	}
	
	public function getUser($username)
    {
    	$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM nn_users WHERE username='$username'");
	}
	
	public function getUsersInsideCircle($username, $lat, $long, $radius)
	{
    	$CI =& get_instance();
		$bbox = "(', "+
					($lat - $radius)+", ' ', "+($long - $radius)+", ',',"+
					($lat + $radius)+", ' ', "+($long - $radius)+", ',',"+
					($lat + $radius)+", ' ', "+($long + $radius)+", ',',"+
					($lat - $radius)+", ' ', "+($long + $radius)+", ',',"+
					($lat - $radius)+", ' ', "+($long - $radius)+", ')";
		return $CI->db->query("SELECT name, X(position) AS x, Y(position) AS y FROM nn_users WHERE Intersects( position, GeomFromText(CONCAT('POLYGON($bbox)')) ) AND SQRT(POW( ABS( X(position) - $lat), 2) + POW( ABS( Y(position) - $long), 2)) < $radius");
	
		/*SET @center = GeomFromText('POINT(10 10)'); 
SET @radius = 30; 
SET @bbox = CONCAT('POLYGON((', 
X(@center) - @radius, ' ', Y(@center) - @radius, ',', 
X(@center) + @radius, ' ', Y(@center) - @radius, ',', 
X(@center) + @radius, ' ', Y(@center) + @radius, ',', 
X(@center) - @radius, ' ', Y(@center) + @radius, ',', 
X(@center) - @radius, ' ', Y(@center) - @radius, '))' 
); 
SELECT name, AsText(location) 
FROM Points 
WHERE Intersects( location, GeomFromText(@bbox) ) 
AND SQRT(POW( ABS( X(location) - X(@center)), 2) + POW( ABS(Y(location) - Y(@center)), 2 )) < @radius; To Obtain a result ordered by distance from the center of the selection area: */
	}
	
	public function verifyPass($username, $password, $updateFlags = true)
	{
		log_message("error", "MYSQL ERROR verifyPass: " . $username . "  -  " . $password);
		$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM nn_users WHERE username='$username' AND password='$password'");
		if($CI->db->_error_number() != 0)
			log_message("error", "MYSQL ERROR verifyPass: " . $CI->db->_error_message() );
		if($CI->db->_error_number() == 0){
			$result = $query->result();
			$CI->session->set_userdata('flags', $result[0]->flags);
			return TRUE;
		}
		else
			return FALSE;
	}
	
	/**********************************/
	/************* JONAS **************/
	/**********************************/
	public function jon_userexists($deviceid)
	{
		$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM jon_users WHERE deviceid='$deviceid'");
		if($query->num_rows() > 1)
			return - 1;
		else
			return $query->num_rows() == 1 ? true : false;
	}
	
	//http://howto-use-mysql-spatial-ext.blogspot.dk/
	public function jon_updatePosition($deviceid, $lat, $long)
	{
		$CI =& get_instance();
		$time = time();
		
		$query = $CI->db->query("UPDATE jon_users SET position=GeomFromText('POINT($lat $long)'), time='$time' WHERE deviceid='$deviceid'");
		return $CI->db->_error_number();
	}
	
	public function jon_initiateUser($deviceid, $name)
	{
		$CI =& get_instance();
		if($this->jon_userexists($deviceid))
		{
			$query = $CI->db->query("UPDATE jon_users SET name='$name' WHERE deviceid='$deviceid'");
			return $CI->db->_error_number();
		}
		else
		{
			$query = $CI->db->query("INSERT INTO jon_users (deviceid, name) VALUES ('$deviceid', '$name')");
			return $CI->db->_error_number();
		}
	}
	
	public function jon_getOtherUsers($mydeviceid)
	{
		$CI =& get_instance();
		$query = $CI->db->query("SELECT name, X(position) x, Y(position) y, time FROM jon_users WHERE deviceid != '$mydeviceid'");
		return $query;
	}
}
/* END OF FILE */