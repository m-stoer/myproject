package io.helidon.examples.quickstart.mp;

public class Person
{
	private String name;
	private int age;

	public Person()
	{
	}

	public Person(final String name, final int age)
	{
		this.name = name;
		this.age = age;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public int getAge()
	{
		return this.age;
	}

	public void setAge(final int age)
	{
		this.age = age;
	}
}
