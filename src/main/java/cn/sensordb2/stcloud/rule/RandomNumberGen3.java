package cn.sensordb2.stcloud.rule;



import com.ugos.jiprolog.engine.*;
import java.util.Hashtable;
import java.util.Random;

public class RandomNumberGen3 extends JIPXCall
{
    Random m_random;

    int m_nMin = -1;
    int m_nMax = -1;

    public boolean unify(JIPCons input, Hashtable varsTbl)
    {
        // input is a List like [Min, Max, Rand]

        // set min max parameters only the first time
        if(m_random == null)
        {
            // initialize the parameters
            m_random = new Random();

            JIPTerm term = input.getNth(1);
            if(term instanceof JIPVariable)
                term = ((JIPVariable)term).getValue();

            JIPNumber min = (JIPNumber)term;

            term = input.getNth(2);
            if(term instanceof JIPVariable)
                term = ((JIPVariable)term).getValue();

            JIPNumber max = (JIPNumber)term;

            m_nMin = (int)min.getDoubleValue();
            m_nMax = (int)max.getDoubleValue();
        }

        int n = (int)(m_random.nextDouble() * (m_nMax - m_nMin)) + m_nMin;
        JIPNumber rand = JIPNumber.create(n);

        return input.getNth(3).unify(rand, varsTbl);
    }

    public boolean hasMoreChoicePoints()
    {
        return false;
    }
}