<?php
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
			return true;
		else
			return false;
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
		$query = $CI->db->query("INSERT INTO app_users (username, position, bluetoothid, password) VALUES ('$username', GeomFromText('POINT($lat $long)'), '$bluetoothID', '$password')");
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
	
	public function jon_deleteNotification($deviceid, $realid){
		$CI =& get_instance();
		$query = $CI->db->query("DELETE FROM jon_notifications WHERE deviceid = '$deviceid' AND realid = '$realid'");
		return $CI->db->_error_number();
	}
	
	public function jon_createNotification($deviceid, $realid, $message)
	{
		$CI =& get_instance();
		if($this->jon_notificationexists($deviceid, $realid))
		{
			$query = $CI->db->query("UPDATE jon_notifications SET message='$message' WHERE deviceid='$deviceid' AND realid='$realid'");
			return $CI->db->_error_number();
		} else {
			$query = $CI->db->query("INSERT INTO jon_notifications (deviceid, realid, message) VALUES ('$deviceid', '$realid', '$message')");
			return $CI->db->_error_number();
		}
	}
	
	public function jon_getNotifications($mydeviceid)
	{
		$CI =& get_instance();
		$query = $CI->db->query("SELECT realid, message FROM jon_notifications WHERE deviceid = '$mydeviceid'");
		return $query;
	}
	
	public function jon_notificationexists($deviceid, $realid)
	{
		$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM jon_notifications WHERE deviceid='$deviceid' AND realid='$realid'");
		if($query->num_rows() > 1)
			return - 1;
		else
			return $query->num_rows() == 1 ? true : false;
	}
	
	public function jon_deleteAssociation($deviceid, $realid){
		$CI =& get_instance();
		$query = $CI->db->query("DELETE FROM jon_associations WHERE deviceid = '$deviceid' AND realid = '$realid'");
		return $CI->db->_error_number();
	}
	
	public function jon_getAssociations($mydeviceid, $type)
	{
		$CI =& get_instance();
		$query = $CI->db->query("SELECT realid, nameid FROM jon_associations WHERE deviceid = '$mydeviceid' AND type = '$type'");
		return $query;
	}
	
	public function jon_createAssociation($deviceid, $realid, $nameid, $type)
	{
		$CI =& get_instance();
		if($this->jon_associationexists($deviceid, $realid))
		{
			$query = $CI->db->query("UPDATE jon_associations SET nameid='$nameid', type='$type' WHERE deviceid='$deviceid' AND realid='$realid'");
			return $CI->db->_error_number();
		} else {
			$query = $CI->db->query("INSERT INTO jon_associations (deviceid, realid, nameid, type) VALUES ('$deviceid', '$realid', '$nameid', '$type')");
			return $CI->db->_error_number();
		}
	}
	
	public function jon_associationexists($deviceid, $realid)
	{
		$CI =& get_instance();
		$query = $CI->db->query("SELECT * FROM jon_associations WHERE deviceid='$deviceid' AND realid='$realid'");
		if($query->num_rows() > 1)
			return - 1;
		else
			return $query->num_rows() == 1 ? true : false;
	}
	
	public function jon_deleteUser($deviceid){
		$CI =& get_instance();
		$query = $CI->db->query("DELETE FROM jon_users WHERE deviceid = '$deviceid'");
		return $CI->db->_error_number();
	}
}
/* END OF FILE */