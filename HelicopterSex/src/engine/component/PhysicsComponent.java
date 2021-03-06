package engine.component;

import engine.GameTime;
import engine.utility.MathHelper;
import engine.utility.Vector2;

public class PhysicsComponent 
extends ActorComponent
{
	public enum Direction {FORWARD, BACKWARD, LEFT, RIGHT};


	// ******************** Fields ******************** 
	
	// Translation fields.
	public Vector2 velocity = new Vector2();
	public Vector2 acceleration = new Vector2();
	public float translationDragFactor = 0.98f;
	public float translationThresholdSquared = 2;
	
	// Rotation Fields.
	public float angularVelocity = 0;
	public float angularAcceleration = 0;
	public float rotationDragFactor = 0.96f;
	public float angularSqrtThreshold = 0.2f;
	
	// Movement Fields: Configurable
	public float mass = 7; 
	public float momentOfInertia = 1.5f; 
	
	public float accelerationIntensity = 8000; 
	public float angularAccelerationIntensity = 440; 

	public float maxVelocitySquared = 800; 
	public float maxAngularVelocity = 4;
	
	public float rotateThreshhold = 0.1f;
	
	// Configuration fields.
	public boolean isAbsoluteAcceleration = false;
	
	
	
	
	// ******************** Constructors ******************** 

		
		
		
		
		
	// ******************** Methods ******************** 
	@Override
	public String getName()
	{
		return "physicsComponent";
	}
	
	@Override
	public void onAttach()
	{		
	}
	
	@Override
	public void update(GameTime gameTime)
	{
		// *** Translation!!!
		// Handle drag, and velocity below the threshold.
		velocity.mul(translationDragFactor);
		if(velocity.magnitudeSquared() < translationThresholdSquared)
		{
			velocity.x = 0;
			velocity.y = 0;
		}
		
		// Calculate velocity.
		velocity.add(Vector2.mul(acceleration, gameTime.dt_s()));
		
		// Clamp velocity.
		// TODO : clamp velocity.
		
		// Update position.
		parent.position.add(Vector2.mul(velocity, gameTime.dt_s()));
		
		// Reset acceleration.
		acceleration.x = 0;
		acceleration.y = 0;
		
		
		// *** Rotation!!!
		// Handle drag and angular velocity below the threshold.
		angularVelocity *= rotationDragFactor;
		if(Math.abs(angularVelocity) < angularSqrtThreshold)
		{
			angularVelocity = 0;
		}
		
		// Calculate velocity.
		angularVelocity += angularAcceleration * gameTime.dt_s();
		
		// Clamp rotation.
		if(Math.abs(angularVelocity) > maxAngularVelocity)
		{
			angularVelocity = MathHelper.clamp(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
		}
		
		// Update rotation.
		parent.rotation += angularVelocity * gameTime.dt_s();
		handleRotation();

		// Reset angular acceleration
		angularAcceleration = 0;

	}

	@Override
	public void onDestroy()
	{		
	}

	public Vector2 getForwardVector()
	{
		Vector2 forwardVector = new Vector2(0, -1);
		if(parent.rotation != 0)
		{
			forwardVector.rotate(parent.rotation);
		}
		
		return forwardVector;
	}
	
	public void accelerate(Direction direction)
	{
		Vector2 forward = new Vector2(0, -1);
		
		if(isAbsoluteAcceleration == false)
		{
			forward = getForwardVector();
		}
		forward.mul(accelerationIntensity / mass);
		


		if(direction == Direction.LEFT)
		{
			forward.rotate((float)(-Math.PI / 2));
		}
		else if(direction == Direction.RIGHT)
		{

			forward.rotate((float)(Math.PI / 2));
		}
		else if(direction == Direction.BACKWARD)
		{
			forward.mul(-1);
		}
		
		acceleration.add(forward);
	}
	
	public void rotate(Direction direction)
	{
		if(direction == Direction.LEFT)
		{
			angularAcceleration = angularAccelerationIntensity / momentOfInertia; 
		}
		else
		{
			angularAcceleration = - angularAccelerationIntensity / momentOfInertia;
		}
	}
	
	
	public void rotate(float angle)
	{
		if(angle > rotateThreshhold)
		{
			angularAcceleration = angularAccelerationIntensity / momentOfInertia;
		}
		else if(angle < -rotateThreshhold)
		{
			angularAcceleration = - angularAccelerationIntensity / momentOfInertia;
		}
		else
		{
			parent.rotation += angle;
		}
	}

	private void handleRotation()
	{
		if(parent.rotation < 0)
		{
			parent.rotation += 2 * Math.PI;
		}
		else if(parent.rotation > 2 * Math.PI)
		{
			parent.rotation -= 2 *  Math.PI;
		}
	}

	@Override
	public ActorComponent clone()
	{		
		PhysicsComponent clone = new PhysicsComponent();
		clone.mass = mass;
		clone.momentOfInertia = momentOfInertia;
		clone.accelerationIntensity = accelerationIntensity;
		clone.angularAccelerationIntensity = angularAccelerationIntensity;
		clone.maxVelocitySquared = maxVelocitySquared;
		clone.maxAngularVelocity = maxAngularVelocity;
		
		return clone;	
	}
	
}
